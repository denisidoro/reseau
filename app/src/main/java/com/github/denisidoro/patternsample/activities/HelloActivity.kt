package com.github.denisidoro.patternsample.activities

import com.github.denisidoro.patternsample.R
import com.github.denisidoro.patternsample.controllers.counter.CounterController
import com.github.denisidoro.reseau.activity.ControllerActivity

class HelloActivity : ControllerActivity<CounterController>() {
    override val layoutRes = R.layout.activity_hello
    override fun createController() = CounterController(this)
}

