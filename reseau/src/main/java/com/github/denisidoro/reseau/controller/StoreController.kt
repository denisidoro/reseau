package com.github.denisidoro.reseau.controller

import android.support.annotation.CallSuper
import com.github.denisidoro.reseau.behaviors.HasStore
import com.github.denisidoro.reseau.behaviors.StoreBehavior
import redux.api.Reducer
import redux.api.Store

abstract class StoreController<S : Any>(
        private val controller: Controller,
        private val storeBehavior: HasStore<S>)
    : Controller by controller, HasStore<S> by storeBehavior {

    constructor(reducer: Reducer<S>, initialState: S, enhancer: Store.Enhancer<S> = Store.Enhancer { it }) : this(SimpleController(), object : StoreBehavior<S>() {
        override fun getReducer(): Reducer<S> = reducer
        override fun getInitialState(): S = initialState
        override fun getEnhancer(): Store.Enhancer<S> = enhancer
    })

    override fun dispatchLocal(action: Any): Any = store.dispatch(action)

    @CallSuper
    override fun unsubscribe() {
        super.unsubscribe()
        controller.unsubscribe()
    }

}
