package com.github.denisidoro.reseau.viewbinder

import android.content.Context
import android.view.ViewGroup
import rx.subscriptions.CompositeSubscription
import java.lang.ref.WeakReference

abstract class ViewBinder<in M>(
        private val rootRef: WeakReference<ViewGroup>,
        private val dispatch: (Any) -> Any) {

    constructor(root: ViewGroup, dispatch: (Any) -> Any) : this(WeakReference(root), dispatch)

    val root: ViewGroup
        get() = rootRef.get()

    val context: Context
        get() = root.context

    val compositeSubscription = CompositeSubscription()

    open fun onCreate() {}

    abstract fun bind(viewModel: M)

    fun unsubscribe() {
        compositeSubscription.clear()
    }

}
