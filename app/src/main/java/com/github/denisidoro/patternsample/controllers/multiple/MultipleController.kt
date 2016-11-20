package com.github.denisidoro.patternsample.controllers.multiple

import com.github.denisidoro.patternsample.controllers.counter.CounterController
import com.github.denisidoro.revvm.activity.ControllerActivity
import com.github.denisidoro.revvm.controller.Controller
import com.github.denisidoro.revvm.controller.SimpleController

class MultipleController(activity: ControllerActivity<*>) : SimpleController() {

    val counter1 = CounterController(activity)
    val counter2 = CounterController(activity)
    val counter3 = CounterController(activity)

    override val children: List<Controller> = listOf(counter1, counter2)

}
