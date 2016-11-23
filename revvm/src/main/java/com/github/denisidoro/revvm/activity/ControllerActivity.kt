package com.github.denisidoro.revvm.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import com.github.denisidoro.revvm.controller.Controller

abstract class ControllerActivity<out C : Controller> : BaseActivity() {

    protected val controller: C by lazy { createController() }
    abstract protected val layoutRes: Int

    abstract fun createController(): C

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        controller.onCreate()
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        controller.onResume()
    }

    @CallSuper
    override fun onPause() {
        controller.onPause()
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        controller.onStop()
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        controller.onDestroy()
        super.onDestroy()
    }

}

