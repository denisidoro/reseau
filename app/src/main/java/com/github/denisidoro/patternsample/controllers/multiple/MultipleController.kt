package com.github.denisidoro.patternsample.controllers.multiple

import com.github.denisidoro.patternsample.activities.MultipleActivity
import com.github.denisidoro.patternsample.controllers.counter.CounterController
import com.github.denisidoro.patternsample.controllers.log.LogController
import com.github.denisidoro.reseau.controller.Controller
import com.github.denisidoro.reseau.controller.HasState
import com.github.denisidoro.reseau.controller.HolderController
import rx.Observable

class MultipleController(activity: MultipleActivity) : HolderController(), HasState<MultipleState> {

    val counter0 = CounterController(activity, 0)
    val counter1 = CounterController(activity, 1)
    val log = LogController(activity)

    override val children: List<Controller> =
            listOf(counter0, counter1, log)

    override var state = MultipleState(counter0.state, counter1.state)

    override val stateObservable = Observable.combineLatest(
            counter0.stateObservable,
            counter1.stateObservable,
            ::MultipleState)

}
