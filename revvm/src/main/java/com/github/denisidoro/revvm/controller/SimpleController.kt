package com.github.denisidoro.revvm.controller

import android.support.annotation.CallSuper
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class SimpleController : Controller, ActivityLifecycle {

    override val name: String = javaClass.simpleName
    override var parent: Controller? = null
    override val children: List<Controller> = listOf()
    override val observable: Observable<out Any> = Observable.empty()
    val compositeSubscription = CompositeSubscription()

    override fun dispatchLocal(action: Any): Any = {}

    override fun getRoot(): Controller = if (parent == null) this else parent!!.getRoot()

    override fun dispatchRoot(action: Any): Any = getRoot().dispatch(action)

    override fun dispatch(action: Any): Any =
            dispatchLocal(action).let { value ->
                propagate { it.dispatch(action) }
                value
            }

    override fun nodesBelow(): List<Controller> =
            if (children.isEmpty()) listOf(this)
            else children.flatMap { it.nodesBelow() }.plus(this)

    @CallSuper
    override fun unsubscribe() {
        compositeSubscription.unsubscribe()
        propagate { it.unsubscribe() }
    }

    override fun onCreate() {
        propagate { it.parent = this }
    }

    @CallSuper
    override fun onDestroy() {
        unsubscribe()
    }

    protected fun Subscription.register() = compositeSubscription.add(this)

}