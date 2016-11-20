package com.github.denisidoro.revvm.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import com.github.denisidoro.revvm.controller.ActivityLifecycle
import com.github.denisidoro.revvm.controller.Controller
import com.github.denisidoro.revvm.controller.invokeForAll

abstract class ControllerActivity<out C : Controller> : BaseActivity() {

    protected val controller: C by lazy { createController() }
    abstract protected val layoutRes: Int

    abstract fun createController(): C

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        trigger { onCreate() }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        trigger { onStart() }
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        trigger { onResume() }
    }

    @CallSuper
    override fun onPause() {
        trigger { onPause() }
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        trigger { onStop() }
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        trigger { onDestroy() }
        super.onDestroy()
    }

    private fun trigger(f: ActivityLifecycle.() -> Unit) {
        controller.invokeForAll { (it as? ActivityLifecycle)?.f() }
    }

}

