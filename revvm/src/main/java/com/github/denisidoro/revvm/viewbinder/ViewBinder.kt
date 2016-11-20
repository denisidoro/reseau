package com.github.denisidoro.revvm.viewbinder

import android.support.annotation.IdRes
import android.view.ViewGroup
import com.github.denisidoro.revvm.activity.BaseActivity
import rx.subscriptions.CompositeSubscription

abstract class ViewBinder<M>(val root: ViewGroup, val dispatch: (Any) -> Any) {

    constructor(activity: BaseActivity, @IdRes resourceId: Int, dispatch: (Any) -> Any) : this(activity.findViewById(resourceId) as ViewGroup, dispatch)
    constructor(activity: BaseActivity, dispatch: (Any) -> Any) : this(activity.rootView as ViewGroup, dispatch)

    //constructor(root: ViewGroup) : this(WeakReference(root))

    //fun getRoot(): ViewGroup = rootRef.get()
    //fun getContext(): Context = getRoot().context
    val context = root.context

    val compositeSubscription = CompositeSubscription()

    abstract fun bind(viewModel: M)

    fun unsubscribe() {
        compositeSubscription.clear()
    }

}
