package com.github.denisidoro.patternsample.hello

import com.github.denisidoro.patternsample.hello.HelloActions.MINUS
import com.github.denisidoro.patternsample.hello.HelloActions.PLUS
import com.github.denisidoro.revvm.controller.LegoStoreController
import com.github.denisidoro.revvm.redux.SimpleReducer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class HelloController(activity: HelloActivity) : LegoStoreController<HelloState, HelloActivity, HelloViewModel, HelloViewBinder>(activity) {

    override fun getInitialState() = HelloState(13)

    override fun getReducer() = SimpleReducer { state: HelloState, action: Any ->
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
                .map(::HelloViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { emitViewModel(it) }
                .register()
    }

    override fun createViewBinder(activity: HelloActivity, dispatch: (Any) -> Any) = HelloViewBinder(activity, dispatch)

}
