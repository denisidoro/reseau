package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.redux.RxStore
import rx.Observable

abstract class BaseStoreController<S: Any> : BaseController() {

    abstract val store: RxStore<S>

    override val observable: Observable<S> = store.observable

    override fun dispatchLocal(action: Any) = store.dispatch(action)

}
