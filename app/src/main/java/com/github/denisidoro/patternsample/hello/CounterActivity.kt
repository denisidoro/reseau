package com.github.denisidoro.patternsample.hello

import com.github.denisidoro.patternsample.R
import com.github.denisidoro.revvm.activity.ControllerActivity

class CounterActivity : ControllerActivity<CounterController>() {

    override val layoutRes = R.layout.activity_hello

    override fun createController() = CounterController(this)

}
