package com.github.denisidoro.reseau.viewbinder

import android.content.Context
import android.view.ViewGroup
import rx.subscriptions.CompositeSubscription

abstract class ViewBinder<in M>(val root: ViewGroup, val dispatch: (Any) -> Any) {

    //constructor(root: ViewGroup) : this(WeakReference(root))
    //fun getRoot(): ViewGroup = rootRef.get()
    //fun getContext(): Context = getRoot().context

    val context: Context
        get() = root.context

    val compositeSubscription = CompositeSubscription()

    open fun onCreate() {}

    abstract fun bind(viewModel: M)

    fun unsubscribe() {
        compositeSubscription.clear()
    }

}
