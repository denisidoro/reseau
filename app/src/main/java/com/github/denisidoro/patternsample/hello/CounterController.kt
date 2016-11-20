package com.github.denisidoro.patternsample.hello

import com.github.denisidoro.patternsample.hello.CounterActions.MINUS
import com.github.denisidoro.patternsample.hello.CounterActions.PLUS
import com.github.denisidoro.revvm.controller.LegoStoreController
import com.github.denisidoro.revvm.redux.SimpleReducer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class CounterController(activity: CounterActivity) : LegoStoreController<CounterState, CounterActivity, CounterViewModel, CounterViewBinder>(activity) {

    override fun getInitialState() = CounterState(13)

    override fun getReducer() = SimpleReducer { state: CounterState, action: Any ->
        when (action) {
            is MINUS -> state.copy(i = state.i - 1)
            is PLUS -> state.copy(i = state.i + 1)
            else -> state
        }
    }

    override fun onCreate() {
        super.onCreate()

        observable
                .distinctUntilChanged()
                .map(::CounterViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { emitViewModel(it) }
                .register()
    }

    override fun createViewBinder(activity: CounterActivity, dispatch: (Any) -> Any) = CounterViewBinder(activity, dispatch)

}
