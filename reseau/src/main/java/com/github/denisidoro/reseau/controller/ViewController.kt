package com.github.denisidoro.reseau.controller

import android.support.annotation.IdRes
import android.view.ViewGroup
import com.github.denisidoro.reseau.activity.BaseActivity
import com.github.denisidoro.reseau.behaviors.HasView
import com.github.denisidoro.reseau.viewbinder.ViewBinder
import com.github.denisidoro.reseau.viewmodel.ViewModel
import rx.subjects.PublishSubject

abstract class ViewController<A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        activity: A,
        rootView: ViewGroup)
    : ActivityController<A>(activity), HasView<M, B> {

    constructor(activity: A, @IdRes resourceId: Int) : this(activity, activity.findViewById(resourceId) as ViewGroup)
    constructor(activity: A) : this(activity, activity.rootView as ViewGroup)

    protected val viewBinder: B by lazy { createViewBinder(rootView) { dispatch(it, this, false) } }
    private val viewModelSubject by lazy { PublishSubject.create<M>() }

    override fun onCreate() {
        super.onCreate()

        viewModelSubject
                .distinctUntilChanged()
                .doOnNext { viewBinder.bind(it) }
                .subscribe()
                //.register()
    }

    override fun unsubscribe() {
        viewBinder.unsubscribe()
        super.unsubscribe()
    }

    override fun emitViewModel(viewModel: M) {
        viewModelSubject.onNext(viewModel)
    }

}