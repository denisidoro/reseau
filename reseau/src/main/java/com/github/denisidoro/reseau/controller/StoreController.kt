package com.github.denisidoro.reseau.controller

import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.view.ViewGroup
import com.github.denisidoro.reseau.activity.BaseActivity
import com.github.denisidoro.reseau.behaviors.HasState
import com.github.denisidoro.reseau.redux.RxStore
import com.github.denisidoro.reseau.viewbinder.ViewBinder
import com.github.denisidoro.reseau.viewmodel.ViewModel
import redux.api.Reducer
import redux.api.Store
import rx.Observable

abstract class StoreController<S : Any>: Controller(), HasState<S> {

    val store: RxStore<S> by lazy { RxStore(getReducer(), getInitialState(), getEnhancer()) }

    override var state: S = store.state
        get() = store.state

    override val stateObservable: Observable<S> by lazy { store.observable.startWith(store.state) }

    abstract fun getReducer(): Reducer<S>

    abstract fun getInitialState(): S

    open fun getEnhancer(): Store.Enhancer<S> = Store.Enhancer { it }

    override fun dispatchLocal(action: Any) = store.dispatch(action)

    @CallSuper
    override fun unsubscribe() {
        store.unsubscribe()
        super.unsubscribe()
    }

}
