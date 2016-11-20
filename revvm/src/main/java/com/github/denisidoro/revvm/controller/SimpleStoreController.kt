package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.redux.RxStore
import rx.Observable

abstract class SimpleStoreController<S: Any> : SimpleController() {

    abstract val store: RxStore<S>

    override val observable: Observable<S> = store.observable

    override fun dispatchLocal(action: Any) = store.dispatch(action)

}
