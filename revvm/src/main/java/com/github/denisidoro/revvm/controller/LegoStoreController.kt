package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.activity.BaseActivity
import com.github.denisidoro.revvm.redux.RxStore
import com.github.denisidoro.revvm.viewbinder.ViewBinder
import com.github.denisidoro.revvm.viewmodel.ViewModel
import rx.Observable
import java.lang.ref.WeakReference

abstract class LegoStoreController<S: Any, A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        activityRef: WeakReference<A>) : LegoController<A, M, B>(activityRef), StoreController {

    constructor(activity: A) : this(WeakReference(activity))

    abstract override val store: RxStore<S>

    override val observable: Observable<S> = store.observable

    override fun dispatchLocal(action: Any) = store.dispatch(action)

}
