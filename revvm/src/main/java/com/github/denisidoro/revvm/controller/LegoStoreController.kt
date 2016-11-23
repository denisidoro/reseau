package com.github.denisidoro.revvm.controller

import android.support.annotation.IdRes
import android.view.ViewGroup
import com.github.denisidoro.revvm.activity.BaseActivity
import com.github.denisidoro.revvm.redux.Reducer
import com.github.denisidoro.revvm.redux.RxStore
import com.github.denisidoro.revvm.viewbinder.ViewBinder
import com.github.denisidoro.revvm.viewmodel.ViewModel
import rx.Observable
import java.lang.ref.WeakReference

abstract class LegoStoreController<S : Any, A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        activityRef: WeakReference<A>,
        root: ViewGroup
) : LegoController<A, M, B>(activityRef, root), HasState<S> {

    constructor(activity: A, @IdRes resourceId: Int) : this(activity, activity.findViewById(resourceId) as ViewGroup)
    constructor(activity: A, root: ViewGroup) : this(WeakReference<A>(activity), root)
    constructor(activity: A) : this(activity, activity.rootView as ViewGroup)

    val store: RxStore<S> by lazy { RxStore(getInitialState(), getReducer()) }

    override var state: S = store.state

    override val stateObservable: Observable<S> by lazy { store.observable.startWith(store.state) }

    abstract fun getReducer(): Reducer<S>

    abstract fun getInitialState(): S

    override fun dispatchSelf(action: Any) = store.dispatch(action)

}
