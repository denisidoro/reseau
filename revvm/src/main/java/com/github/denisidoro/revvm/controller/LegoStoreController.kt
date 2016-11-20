package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.activity.BaseActivity
import com.github.denisidoro.revvm.redux.Reducer
import com.github.denisidoro.revvm.redux.RxStore
import com.github.denisidoro.revvm.viewbinder.ViewBinder
import com.github.denisidoro.revvm.viewmodel.ViewModel
import rx.Observable
import java.lang.ref.WeakReference

abstract class LegoStoreController<S : Any, A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        activityRef: WeakReference<A>) : LegoController<A, M, B>(activityRef), StoreController {

    constructor(activity: A) : this(WeakReference(activity))

    override val store: RxStore<S> by lazy { RxStore(getInitialState(), getReducer()) }

    abstract fun getReducer(): Reducer<S>

    abstract fun getInitialState(): S

    override val observable: Observable<S> by lazy { store.observable.startWith(store.state) }

    override fun dispatchLocal(action: Any) = store.dispatch(action)

}
