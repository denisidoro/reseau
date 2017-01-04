package com.github.denisidoro.reseau.controller

import android.support.annotation.IdRes
import android.view.ViewGroup
import com.github.denisidoro.reseau.activity.BaseActivity
import com.github.denisidoro.reseau.behaviors.HasStore
import com.github.denisidoro.reseau.behaviors.StoreBehavior
import com.github.denisidoro.reseau.viewbinder.ViewBinder
import com.github.denisidoro.reseau.viewmodel.ViewModel
import redux.api.Reducer
import redux.api.Store

abstract class ViewStoreController<S : Any, A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        activity: A,
        rootView: ViewGroup,
        private val storeBehavior: HasStore<S>)
    : ViewController<A, M, B>(activity, rootView), HasStore<S> by storeBehavior {

    constructor(activity: A, rootView: ViewGroup, reducer: Reducer<S>, initialState: S, enhancer: Store.Enhancer<S> = Store.Enhancer { it }) : this(activity, rootView, object : StoreBehavior<S>() {
        override fun getReducer(): Reducer<S> = reducer
        override fun getInitialState(): S = initialState
        override fun getEnhancer(): Store.Enhancer<S> = enhancer
    })

    constructor(activity: A, @IdRes resourceId: Int, reducer: Reducer<S>, initialState: S, enhancer: Store.Enhancer<S> = Store.Enhancer { it }) : this(activity, activity.findViewById(resourceId) as ViewGroup, reducer, initialState, enhancer)

    constructor(activity: A, reducer: Reducer<S>, initialState: S, enhancer: Store.Enhancer<S> = Store.Enhancer { it }) : this(activity, activity.rootView as ViewGroup, reducer, initialState, enhancer)

    override fun unsubscribe() {
        viewBinder.unsubscribe()
        storeBehavior.unsubscribe()
    }

}
