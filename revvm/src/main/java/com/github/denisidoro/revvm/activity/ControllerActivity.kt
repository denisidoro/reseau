package com.github.denisidoro.revvm.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import com.github.denisidoro.revvm.controller.ActivityLifecycle
import com.github.denisidoro.revvm.controller.Controller
import com.github.denisidoro.revvm.controller.invokeAndPropagate

abstract class ControllerActivity<out C : Controller> : BaseActivity() {

    protected val controller: C by lazy { createController() }
    abstract protected val layoutRes: Int

    abstract fun createController(): C

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        controller.invokeAndPropagate { asActivityController()?.onCreate() }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        controller.invokeAndPropagate { asActivityController()?.onStart() }
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        controller.invokeAndPropagate { asActivityController()?.onResume() }
    }

    @CallSuper
    override fun onPause() {
        controller.invokeAndPropagate { asActivityController()?.onPause() }
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        controller.invokeAndPropagate { asActivityController()?.onStop() }
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        controller.invokeAndPropagate { asActivityController()?.onDestroy() }
        super.onDestroy()
    }

    private fun asActivityController() = (controller as? ActivityLifecycle)
}

