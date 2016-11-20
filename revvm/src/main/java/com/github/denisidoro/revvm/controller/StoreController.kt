package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.redux.RxStore
import rx.Observable

abstract class StoreController<S: Any> : Controller() {

    abstract val store: RxStore<S>

    override val observable: Observable<S> = store.observable

    override fun dispatch(action: Any) = store.dispatch(action)

}
