# réseau

[ ![Download](https://api.bintray.com/packages/denisidoro/maven/reseau/images/download.svg) ](https://bintray.com/denisidoro/maven/reseau/_latestVersion)

A highly scalable, reactive, MVVM-like library for Android, powered by [Redux][reduxjs], [RxJava][rxjava] and [Kotlin][kotlin].

It is inspired by [Nubank's Lego][lego] and [Reduks][reduks].

The pattern encourages you to write building blocks —generally composed by a controller, a view binder and a view model—, hereafter called nodes, that satisfy the following principles:
```
1) The state of a node is stored in an object tree within a single store.
2) The only way to change the state is to emit an action, an object describing what happened.
3) To specify how the state tree is transformed by actions, you write pure reducers.
4) The state of your whole activity can be accessed in a single object tree.
```

Then you arrange different nodes as in a graph, e.g.:

![Example use](https://cloud.githubusercontent.com/assets/3226564/20561951/543efcfa-b168-11e6-925d-49c12f343599.png)

## Installation
```groovy
dependencies {
    compile 'com.github.denisidoro:reseau:+' // or x.y.z
}
```

## The pattern in a nutshell

Let's write a simple counter application that consists of a single node.

First, we define a state:
```kotlin
data class CounterState(val i: Int)
```

Then we define actions and a state reducer:
```kotlin
sealed class CounterActions {
    object DECREMENT
    object INCREMENT
}
```
```kotlin
val counterReducer = Reducer { state: CounterState, action: Any ->
    when (action) {
        is DECREMENT -> state.copy(i = state.i - 1)
        is INCREMENT -> state.copy(i = state.i + 1)
        else -> state
    }
}
```

Later on we build a view model, that converts state to view properties:
```kotlin
class CounterViewModel(s: CounterState) : ViewModel {
    val text = s.i.toString()
}
```

Then we define the view binder:
```kotlin
class CounterViewBinder(...) : ViewBinder<CounterViewModel>(...) {

    init {
        with(root) {
            minusBT.setOnClickListener { dispatch(DECREMENT) }
            plusBT.setOnClickListener { dispatch(INCREMENT) }
        }
    }

    override fun bind(viewModel: CounterViewModel) {
        root.countTV.text = viewModel.text
    }

}
```

Finally, we create the controller that holds everything together and has all necessary logic:
```kotlin
class CounterController(...) : ViewStoreController<...>(...) {

    override fun createViewBinder(...) = CounterViewBinder(...)
    override fun getInitialState() = CounterState(13)
    override fun getReducer() = counterReducer

    // called when the activity is created
    override fun onCreate() {
        super.onCreate()
        stateObservable
                .map(::CounterViewModel)
                .subscribe { emitViewModel(it) }
    }

}
```

And connect it to the activity:
```kotlin
class CounterActivity : ControllerActivity<CounterController>() {
    override val layoutRes = R.layout.activity_counter
    override fun createController() = CounterController(this)
}
```

## Combining nodes

Let's reuse the previous counter node and code a screen that has 4 nodes: two independent counters, one for log that keeps track of both values and another one that holds them together, as in the graph below:

![Hierarchy](https://cloud.githubusercontent.com/assets/3226564/20578225/6f2bc292-b1ad-11e6-8998-647268eee9d8.png)

The final result will be as follows:

![Demo](https://cloud.githubusercontent.com/assets/3226564/20483051/3b555cbc-afd7-11e6-86c8-e91c619c5677.gif)

In order to have two counter views, we'll change the activity layout XML and pass an argument to the counter controller so that it can map it to the corresponding layout resource ID.

### Hierarchy setup

Each controller may have a parent or children.

To setup the desired hierarchy we define the root controller's children like this:

```kotlin
class MultipleController(...) : Controller() {
    override val children = listOf(
            CounterController(activity, 0),
            CounterController(activity, 1),
            LogController(activity))
}
```

### Accessing state from other nodes

It's up to you how to expose state between nodes.

You can either pass a getter lambda function to child controllers or define public functions or even prevent it whatsoever, for encapsulation reasons. One native, quick way to do this is to make your root controller extend `RootController` and use an extension function that returns the state observable for a given controller by its name.

If we name our controllers accordingly and do the necessary modifications, our log controller could be as follows:

```kotlin
class LogController(...) : ViewController<...>(...) {

    override fun createViewBinder(...) = LogViewBinder(...)

    override fun onStart() {
        super.onStart()
        Observable.combineLatest(
                stateObservableByName<CounterState>("counter0"),
                stateObservableByName<CounterState>("counter1"),
                { c0, c1 -> Pair(c0, c1) })
                .map { LogViewModel(it.first, it.second) }
                .subscribe { emitViewModel(it) }
    }

}
```

If any exception is thrown in the process, an `Observable.error()` is returned.

The implementation of the Log view binder and view model are similar to the ones before.

## Dispatch range

If we start the app like so, clicking on a button of the second counter will interfere with the value from the first one, because both of them have state that is reduced by the same events. To prevent this, we can restrict the dispatch range:
```kotlin
class CounterController(...) : ViewStoreController<...>(...) {
    // ...
    override val dispatchRange = DispatchRange.SELF
}
```
- `SELF`: the dispatch will possibly reduce only the state of the node that dispatched the action;
- `DOWN`: the dispatch will possibly reduce the state of the node that dispatched the action and will be propagated to child nodes downstream;
- `TOP_DOWN` (default): the dispatch will possibly reduce the state of the root node and will be propagated to child nodes downstream.

## Controller types
- `Controller`: simple, stateless, it has no view;
- `ViewController`: stateless, it represents a view with a view binder and a view model;
- `ViewStoreController`: same as above, but stateful;
- `RootController`: stateless by default, it can represent the state of your activity in a single object tree.

## What about all those redux libraries I know and love?
RxJava operators like *map()*, *filter()*, *debounce()*, *combineLatest()* or *distinctUntilChanged()* can replace some libraries such as *reselect* or *redux-debounce*.

## Trivia

### Etymology
  < *re*, as in redux, reactive; and *réseau*, which means network in French.

### To do
- [ ] Tests
- [x] Publish lib
- [ ] Implement middlewares

[frp]: https://gist.github.com/staltz/868e7e9bc2a7b8c1f754
[kotlin]: https://kotlinlang.org/
[reduxjs]: http://redux.js.org/
[react]: https://facebook.github.io/react/
[reframe]: https://github.com/Day8/re-frame
[astut]: https://www.sitepoint.com/12-android-tutorials-beginners/
[lego]: https://github.com/nubank/lego
[reduks]: https://github.com/beyondeye/Reduks
[anvil]: https://github.com/zserge/anvil
[rxjava]: https://github.com/ReactiveX/RxJava
[dagger]: https://github.com/square/dagger
[okhttp]: http://square.github.io/okhttp/
[gson]: https://github.com/google/gson
[mockito]: http://mockito.org/
[robolectric]: http://robolectric.org/
[espresso]: https://google.github.io/android-testing-support-library/docs/espresso/
