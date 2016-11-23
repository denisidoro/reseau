package com.github.denisidoro.patternsample.activities

import com.github.denisidoro.patternsample.R
import com.github.denisidoro.patternsample.controllers.multiple.MultipleController
import com.github.denisidoro.reseau.activity.ControllerActivity

class MultipleActivity : ControllerActivity<MultipleController>() {
    override val layoutRes = R.layout.activity_multiple
    override fun createController() = MultipleController(this)
}
