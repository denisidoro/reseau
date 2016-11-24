package com.github.denisidoro.reseau.controller

import android.support.annotation.CallSuper
import com.github.denisidoro.reseau.exception.ParentAlreadySetException
import com.github.denisidoro.reseau.lifecycle.HasActivityLifecycle
import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class Controller: HasActivityLifecycle {

    enum class DispatchRange {
        SELF, DOWN, TOP_DOWN
    }

    open val name: String = javaClass.simpleName.replace("Controller", "").decapitalize()

    var parent: Controller? = null
        set(value) {
            field?.let { throw ParentAlreadySetException() }
            field = value
        }

    protected open val children: List<Controller> = listOf()

    val compositeSubscription = CompositeSubscription()

    open val dispatchRange: DispatchRange = DispatchRange.TOP_DOWN

    fun getRoot(): Controller = if (parent == null) this else parent!!.getRoot()

    open protected fun dispatchSelf(action: Any): Any = action

    fun dispatchRoot(action: Any): Any = getRoot().dispatch(action)

    fun dispatchChildren(action: Any): Any = getRoot().dispatch(action)

    fun dispatch(action: Any, caller: Controller = this): Any {
            return when (dispatchRange) {
            DispatchRange.SELF -> dispatchSelf(action)
            DispatchRange.DOWN -> {
                dispatchSelf(action).let {
                    children.forEach { dispatch(action, caller) }
                    it
                }
            }
            DispatchRange.TOP_DOWN -> {
                if (caller == this) {
                    getRoot().dispatch(action)
                } else {
                    dispatchSelf(action).let {
                        children.forEach { dispatch(action, caller) }
                        it
                    }
                }
            }
        }
    }

    fun nodesBelow(): List<Controller> =
            if (children.isEmpty()) listOf(this)
            else listOf(this).plus(children.flatMap { it.nodesBelow() })

    @CallSuper
    open fun unsubscribe() {
        compositeSubscription.unsubscribe()
        propagate { it.unsubscribe() }
    }

    protected fun Subscription.register() = compositeSubscription.add(this)

    fun Controller.propagate(f: (Controller) -> Unit) = children.forEach(f)

    @CallSuper
    override fun onCreate() {
        propagate { it.parent = this }
        propagate { it.onCreate() }
    }

    @CallSuper
    override fun onStart() {
        propagate { it.onStart() }
    }

    @CallSuper
    override fun onResume() {
        propagate { it.onResume() }
    }

    @CallSuper
    override fun onPause() {
        propagate { it.onPause() }
    }

    @CallSuper
    override fun onStop() {
        propagate { it.onStop() }
    }

    @CallSuper
    override fun onDestroy() {
        unsubscribe()
        propagate { onDestroy() }
    }

}