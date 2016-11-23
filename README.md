# réseau

A highly-scalable, reactive, MVVM-like library for Android, powered by [Redux][reduxjs], [RxJava][rxjava] and [Kotlin][kotlin]. It is based on [Nubank's Lego][lego] and [Reduks][reduks].

The pattern encourages you to write building blocks --composed by a controller, a view binder and a view model--, hereafter called *Lego*, that satisfy the following principles:
```
1) The state of your whole Lego is stored in an object tree within a single store.
2) The only way to change the state is to emit an action, an object describing what happened.
3) To specify how the state tree is transformed by actions, you write pure reducers.
```

Then you arrange different *Legos* as nodes in a graph. 

![untitled diagram](https://cloud.githubusercontent.com/assets/3226564/20561951/543efcfa-b168-11e6-925d-49c12f343599.png)

### The pattern in a nutshell

Let's write a simple counter application that consists of a single *Lego*.

First, we define a state:
```kotlin
data class CounterState(val i: Int)
```

Then we define actions and their reducers:
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

Later on we a view model, that convert state to view properties:
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
class CounterController(...) : LegoStoreController<...>(...) {

    override fun createViewBinder(...) = CounterViewBinder(root, dispatch)
    override fun getInitialState() = CounterState(13)
    override fun getReducer() = counterReducer

    override fun onCreate() {
        super.onCreate()
        stateObservable
                .distinctUntilChanged()
                .map(::CounterViewModel)
                .subscribe { emitViewModel(it) }
    }

}
```

And bind it to the activity:
```kotlin
class CounterActivity : ControllerActivity<CounterController>() {
    override val layoutRes = R.layout.activity_counter
    override fun createController() = CounterController(this)
}
```

### Etymology
  < *re*, as in redux, reactive; and réseau, which means network in French.
  
### To do
  - Tests
  - Implement middlewares

### Screenshots

![demo](https://cloud.githubusercontent.com/assets/3226564/20483051/3b555cbc-afd7-11e6-86c8-e91c619c5677.gif)

  
[frp]: https://gist.github.com/staltz/868e7e9bc2a7b8c1f754
[kotlin]: https://kotlinlang.org/
[reduxjs]: http://redux.js.org/
[react]: https://facebook.github.io/react/
[reframe]: https://github.com/Day8/re-frame
[astut]: https://www.sitepoint.com/12-android-tutorials-beginners/
[lego]: htts://github.com/nubank/lego
[reduks]: https://github.com/beyondeye/Reduks
[anvil]: https://github.com/zserge/anvil
[rxjava]: https://github.com/ReactiveX/RxJava
[dagger]: https://github.com/square/dagger
[okhttp]: http://square.github.io/okhttp/
[gson]: https://github.com/google/gson
[mockito]: http://mockito.org/
[robolectric]: http://robolectric.org/
[espresso]: https://google.github.io/android-testing-support-library/docs/espresso/
