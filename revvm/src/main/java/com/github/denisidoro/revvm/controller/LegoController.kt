package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.activity.BaseActivity
import com.github.denisidoro.revvm.viewbinder.ViewBinder
import com.github.denisidoro.revvm.viewmodel.ViewModel
import rx.Subscription
import rx.subjects.PublishSubject
import java.lang.ref.WeakReference

abstract class LegoController<S : Any, A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        private val activityRef: WeakReference<A>) : Controller() {

    constructor(activity: A) : this(WeakReference(activity))

    private val viewBinder: B by lazy { createViewBinder() }
    private val viewModelSubject by lazy { PublishSubject.create<M>() }
    var viewModelSubscription: Subscription? = null

    abstract fun createViewBinder(): B

    fun listen() {
        viewModelSubscription = viewModelSubject
                .distinctUntilChanged()
                .subscribe { viewBinder.bind(it) }
    }

    override fun unsubscribe() {
        viewBinder.unsubscribe()
        viewModelSubscription?.unsubscribe()
        super.unsubscribe()
    }

    fun emitViewModel(viewModel: M) {
        viewModelSubject.onNext(viewModel)
    }

    fun getActivity(): A = activityRef.get()

}
