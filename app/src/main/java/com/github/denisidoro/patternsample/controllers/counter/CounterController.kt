package com.github.denisidoro.patternsample.controllers.counter

import android.view.ViewGroup
import com.github.denisidoro.patternsample.R
import com.github.denisidoro.patternsample.controllers.counter.CounterActions.DECREMENT
import com.github.denisidoro.patternsample.controllers.counter.CounterActions.INCREMENT
import com.github.denisidoro.reseau.activity.ControllerActivity
import com.github.denisidoro.reseau.behaviors.DispatchGroup.SELF
import com.github.denisidoro.reseau.controller.ViewStoreController
import redux.api.Dispatcher
import redux.api.Reducer
import redux.api.Store
import redux.api.enhancer.Middleware
import redux.applyMiddleware
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class CounterController(activity: ControllerActivity<*>, id: Int = 0) :
        ViewStoreController<CounterState, ControllerActivity<*>, CounterViewModel, CounterViewBinder>(activity, getLayoutId(id)) {

    override fun getInitialState() = CounterState(13)

    override fun getReducer() = Reducer { state: CounterState, action: Any ->
        when (action) {
            is DECREMENT -> state.copy(i = state.i - 1)
            is INCREMENT -> state.copy(i = state.i + 1)
            else -> state
        }
    }

    private fun navigationMiddleware() = Middleware<CounterState> { store: Store<CounterState>, next: Dispatcher, action: Any ->
        val result = next.dispatch(action)
        result
    }

    override fun getEnhancer(): Store.Enhancer<CounterState> = applyMiddleware(navigationMiddleware())

    override fun onCreate() {
        super.onCreate()
        stateObservable
                .distinctUntilChanged()
                .map(::CounterViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { emitViewModel(it) }
                .register()
    }

    override fun createViewBinder(rootView: ViewGroup, dispatch: (Any) -> Any) =
            CounterViewBinder(rootView, dispatch)

    override val name = "${super.name}$id"

    override val dispatchGroup = SELF

}

fun getLayoutId(id: Int): Int = when (id) {
    0 -> R.id.counter0
    1 -> R.id.counter1
    else -> R.id.counter0
}
