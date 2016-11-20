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
        controller.invokeAndPropagate { asActivityController(it)?.onCreate() }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        controller.invokeAndPropagate { asActivityController(it)?.onStart() }
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        controller.invokeAndPropagate { asActivityController(it)?.onResume() }
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        controller.invokeAndPropagate { asActivityController(it)?.onPause() }
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        controller.invokeAndPropagate { asActivityController(it)?.onStop() }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        controller.invokeAndPropagate { asActivityController(it)?.onDestroy() }
    }

    private fun asActivityController(it: Controller) = (it as? ActivityLifecycle)
}

