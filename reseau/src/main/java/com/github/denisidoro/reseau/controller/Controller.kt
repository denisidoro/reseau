package com.github.denisidoro.reseau.controller

import android.support.annotation.CallSuper
import com.github.denisidoro.reseau.exception.ObservableCastException
import com.github.denisidoro.reseau.exception.ParentAlreadySetException
import com.github.denisidoro.reseau.exception.ReseauException
import com.github.denisidoro.reseau.exception.RootCastException
import com.github.denisidoro.reseau.lifecycle.HasActivityLifecycle
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class Controller : HasActivityLifecycle {

    enum class DispatchRange {
        SELF, DOWN, TOP_DOWN
    }

    open val name: String = javaClass.simpleName.replace("Controller", "").decapitalize()

    private val compositeSubscription = CompositeSubscription()

    open val children: List<Controller> = listOf()

    var parent: Controller? = null
        set(value) {
            field?.let { throw ParentAlreadySetException() }
            field = value
        }

    protected open val dispatchRange: DispatchRange = DispatchRange.TOP_DOWN

    val root: Controller by lazy { parent?.root ?: this }

    open protected fun dispatchLocal(action: Any): Any = action

    fun dispatch(action: Any, caller: Controller = this): Any {
        return when (dispatchRange) {
            DispatchRange.SELF -> dispatchLocal(action)
            DispatchRange.DOWN -> {
                dispatchLocal(action).let {
                    propagate { dispatch(action, caller) }
                    it
                }
            }
            DispatchRange.TOP_DOWN -> {
                if (caller == this) {
                    root.dispatch(action)
                } else {
                    dispatchLocal(action).let {
                        propagate { dispatch(action, caller) }
                        it
                    }
                }
            }
        }
    }

    @CallSuper
    open fun unsubscribe() {
        propagate { it.unsubscribe() }
        compositeSubscription.unsubscribe()
    }

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
        propagate { onDestroy() }
    }

    fun Subscription.register() = compositeSubscription.add(this)

    fun Controller.propagate(f: (Controller) -> Unit) = children.forEach(f)

    infix fun <S> Controller.stateObservableByName(name: String): Observable<S> =
            try {
                val r = root as? RootController ?: throw RootCastException()
                val o = r.stateObservable.map { it[name] }
                when (o) {
                    is Observable<*> -> o as Observable<S>
                    else -> throw ObservableCastException()
                }
            } catch (e: ReseauException) {
                Observable.error(e)
            }

}