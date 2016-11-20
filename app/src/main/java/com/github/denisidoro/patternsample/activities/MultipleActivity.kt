package com.github.denisidoro.patternsample.activities

import com.github.denisidoro.patternsample.R
import com.github.denisidoro.patternsample.controllers.multiple.MultipleController
import com.github.denisidoro.revvm.activity.ControllerActivity

class MultipleActivity : ControllerActivity<MultipleController>() {

    override val layoutRes = R.layout.activity_hello

    override fun createController() = MultipleController(this)

}
