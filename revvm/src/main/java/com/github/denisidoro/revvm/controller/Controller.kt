package com.github.denisidoro.revvm.controller

import android.support.annotation.CallSuper
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class Controller {

    open val parent: Controller? = null
    open val children: List<Controller> = listOf()
    open val observable: Observable<out Any> = Observable.empty()
    val compositeSubscription = CompositeSubscription()

    abstract fun dispatch(action: Any): Any

    fun getRoot(): Controller = parent?.let { this } ?: parent!!.getRoot()

    fun rootDispatch(action: Any): Any = getRoot().dispatch(action)

    fun dispatchDown(action: Any): Any =
        dispatch(action).let { value ->
            propagate { it.dispatchDown(action) }
            value
        }

    @CallSuper
    open fun unsubscribe() {
        compositeSubscription.unsubscribe()
        propagate { it.unsubscribe() }
    }

    protected fun Controller.propagate(f: (Controller) -> Unit) = children.forEach(f)
    protected fun Controller.register(s: Subscription) = compositeSubscription.add(s)

}
