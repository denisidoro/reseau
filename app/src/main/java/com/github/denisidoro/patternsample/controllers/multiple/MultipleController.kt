package com.github.denisidoro.patternsample.controllers.multiple

import com.github.denisidoro.patternsample.activities.MultipleActivity
import com.github.denisidoro.patternsample.controllers.counter.CounterController
import com.github.denisidoro.patternsample.controllers.log.LogController
import com.github.denisidoro.revvm.controller.SimpleController

class MultipleController(activity: MultipleActivity) : SimpleController() {

    val counter0 = CounterController(activity, 0)
    val counter1 = CounterController(activity, 1)
    val log = LogController(activity)

    override val children: List<SimpleController> =
            listOf(counter0, counter1, log)

}
