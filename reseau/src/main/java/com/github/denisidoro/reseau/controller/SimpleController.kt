package com.github.denisidoro.reseau.controller

import android.support.annotation.CallSuper
import com.github.denisidoro.reseau.behaviors.DispatchGroup
import com.github.denisidoro.reseau.exception.ObservableCastException
import com.github.denisidoro.reseau.exception.ParentAlreadySetException
import com.github.denisidoro.reseau.exception.ReseauException
import com.github.denisidoro.reseau.exception.RootCastException
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription

class SimpleController : Controller {

    override val name: String = javaClass.simpleName.replace("Controller", "").decapitalize()

    private val compositeSubscription = CompositeSubscription()

    override val children: List<Controller> = listOf()

    override var parent: Controller? = null
        set(value) {
            field?.let { throw ParentAlreadySetException() }
            field = value
        }

    override val dispatchGroup: Int = DispatchGroup.ALL

    override val root: Controller by lazy { parent?.root ?: this }

    override fun dispatchLocal(action: Any): Any = action

    override fun dispatch(action: Any, caller: Controller, topDown: Boolean): Any =
            when (caller.dispatchGroup) {
                DispatchGroup.SELF -> dispatchLocal(action)
                else -> {
                    if (!topDown) {
                        root.dispatch(action, caller, true)
                    } else {
                        if (caller.dispatchGroup == DispatchGroup.ALL || dispatchGroup == caller.dispatchGroup) {
                            dispatchLocal(action).let {
                                propagate { dispatch(action, caller, true) }
                                it
                            }
                        } else {
                            propagate { dispatch(action, caller, true) }
                        }
                    }
                }
            }


    @CallSuper
    override fun unsubscribe() {
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

    inline fun <reified S> Controller.stateObservableByName(name: String): Observable<S> =
            try {
                val r = root as? RootController<*> ?: throw RootCastException()
                r.stateObservable.map { it[name] as S }.onErrorResumeNext { Observable.error(ObservableCastException()) }
            } catch (e: ReseauException) {
                Observable.error(e)
            }

}