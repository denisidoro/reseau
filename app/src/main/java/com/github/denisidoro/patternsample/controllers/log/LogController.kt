package com.github.denisidoro.patternsample.controllers.log

import android.view.ViewGroup
import com.github.denisidoro.patternsample.controllers.counter.CounterState
import com.github.denisidoro.reseau.activity.ControllerActivity
import com.github.denisidoro.reseau.controller.ViewController
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class LogController(activity: ControllerActivity<*>) :
        ViewController<ControllerActivity<*>, LogViewModel, LogViewBinder>(activity) {

    override fun onStart() {
        super.onStart()

        Observable.combineLatest(
                stateObservableByName<CounterState>("counter0"),
                stateObservableByName<CounterState>("counter1"),
                { c0, c1 -> Pair(c0, c1) })
                .distinctUntilChanged()
                .map { LogViewModel(it.first, it.second) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ emitViewModel(it) }
                .register()
    }

    override fun createViewBinder(rootView: ViewGroup, dispatch: (Any) -> Any) =
            LogViewBinder(rootView, dispatch)

}
