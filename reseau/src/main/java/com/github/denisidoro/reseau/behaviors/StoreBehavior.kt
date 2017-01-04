package com.github.denisidoro.reseau.behaviors

import com.github.denisidoro.reseau.redux.RxStore
import rx.Observable

abstract class StoreBehavior<S : Any> : HasStore<S> {

    override val store: RxStore<S> by lazy { RxStore(getReducer(), getInitialState(), getEnhancer()) }

    override fun getState(): S = store.state

    override val stateObservable: Observable<S> by lazy { store.observable.startWith(store.state) }
}