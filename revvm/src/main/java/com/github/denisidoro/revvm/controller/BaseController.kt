package com.github.denisidoro.revvm.controller

import android.support.annotation.CallSuper
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class BaseController: Controller, ActivityLifecycle {

    override val parent: Controller? = null
    override val children: List<Controller> = listOf()
    override val observable: Observable<out Any> = Observable.empty()
    val compositeSubscription = CompositeSubscription()

    override fun dispatchLocal(action: Any): Any = {}

    override fun getRoot(): Controller = parent?.let { this } ?: parent!!.getRoot()

    override fun rootDispatch(action: Any): Any = getRoot().dispatch(action)

    override fun dispatch(action: Any): Any =
            dispatchLocal(action).let { value ->
                propagate { it.dispatch(action) }
                value
            }

    @CallSuper
    override fun unsubscribe() {
        compositeSubscription.unsubscribe()
        propagate { it.unsubscribe() }
    }

    override fun onDestroy() {
        unsubscribe()
    }

    protected fun Subscription.register() = compositeSubscription.add(this)

}