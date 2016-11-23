package com.github.denisidoro.revvm.controller

import android.support.annotation.CallSuper
import com.github.denisidoro.revvm.exception.ParentAlreadySetException
import com.github.denisidoro.revvm.lifecycle.HasActivityLifecycle
import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class Controller: HasActivityLifecycle {

    enum class DispatchRange {
        SELF, SELF_DOWN, ROOT_DOWN
    }

    open val name: String = javaClass.simpleName.replace("Controller", "").decapitalize()

    var parent: Controller? = null
        set(value) {
            field?.let { throw ParentAlreadySetException() }
            field = value
        }

    protected open val children: List<Controller> = listOf()

    val compositeSubscription = CompositeSubscription()

    open val dispatchRange: DispatchRange = DispatchRange.ROOT_DOWN

    fun getRoot(): Controller = if (parent == null) this else parent!!.getRoot()

    open protected fun dispatchSelf(action: Any): Any = action

    fun dispatchRoot(action: Any): Any = getRoot().dispatch(action)

    fun dispatchChildren(action: Any): Any = getRoot().dispatch(action)

    fun dispatch(action: Any): Any {
        return when (dispatchRange) {
            DispatchRange.SELF -> dispatchSelf(action)
            DispatchRange.SELF_DOWN -> {
                dispatchSelf(action).let {
                    children.forEach { dispatch(action) }
                    it
                }
            }
            DispatchRange.ROOT_DOWN -> getRoot().dispatch(action)
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