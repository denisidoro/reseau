package com.github.denisidoro.patternsample.controllers.counter

import android.view.ViewGroup
import com.github.denisidoro.patternsample.R
import com.github.denisidoro.patternsample.controllers.counter.CounterActions.DECREMENT
import com.github.denisidoro.patternsample.controllers.counter.CounterActions.INCREMENT
import com.github.denisidoro.reseau.activity.ControllerActivity
import com.github.denisidoro.reseau.controller.ViewStoreController
import com.github.denisidoro.reseau.redux.Reducer
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

    override fun createViewBinder(root: ViewGroup, dispatch: (Any) -> Any) =
            CounterViewBinder(root, dispatch)

    override val name = "${super.name}$id"

    override val dispatchRange = DispatchRange.SELF

}

fun getLayoutId(id: Int): Int = when (id) {
    0 -> R.id.counter0
    1 -> R.id.counter1
    else -> R.id.counter0
}
