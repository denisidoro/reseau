package com.github.denisidoro.patternsample.hello

import com.github.denisidoro.patternsample.R
import com.github.denisidoro.revvm.activity.ControllerActivity

class HelloActivity : ControllerActivity<HelloController>() {

    override val layoutRes = R.layout.activity_hello

    override fun createController() = HelloController(this)

}
