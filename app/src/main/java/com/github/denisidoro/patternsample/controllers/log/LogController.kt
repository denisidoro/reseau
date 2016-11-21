package com.github.denisidoro.patternsample.controllers.log

import android.view.ViewGroup
import com.github.denisidoro.patternsample.controllers.counter.CounterState
import com.github.denisidoro.revvm.activity.ControllerActivity
import com.github.denisidoro.revvm.controller.LegoController
import com.github.denisidoro.revvm.controller.SimpleController
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class LogController(activity: ControllerActivity<*>) :
        LegoController<ControllerActivity<*>, LogViewModel, LogViewBinder>(activity) {

    override fun onStart() {
        super.onStart()

        val obs0 = count0Observable()
        val obs1 = count1Observable()
        Observable.combineLatest(
                obs0, obs1,
                { c0, c1 -> Pair(c0, c1) })
                .distinctUntilChanged()
                .map { LogViewModel(it.first, it.second) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { emitViewModel(it) }
                .register()
    }

    // temporary
    private fun count0Observable() = (getRoot() as SimpleController).getState("CounterController0") as Observable<CounterState>
    private fun count1Observable() = (getRoot() as SimpleController).getState("CounterController1") as Observable<CounterState>

    override fun createViewBinder(root: ViewGroup, dispatch: (Any) -> Any) =
            LogViewBinder(root, dispatch)

}
