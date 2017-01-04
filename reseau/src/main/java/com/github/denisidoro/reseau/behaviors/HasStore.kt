package com.github.denisidoro.reseau.behaviors

import android.support.annotation.CallSuper
import com.github.denisidoro.reseau.redux.RxStore
import redux.api.Reducer
import redux.api.Store

interface HasStore<S : Any> : HasState<S> {

    val store: RxStore<S>

    fun getReducer(): Reducer<S>

    fun getInitialState(): S

    fun getEnhancer(): Store.Enhancer<S>

    @CallSuper
    fun unsubscribe() {
        store.unsubscribe()
    }

}