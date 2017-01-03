package com.github.denisidoro.reseau.controller

import android.support.annotation.IdRes
import android.view.ViewGroup
import com.github.denisidoro.reseau.activity.BaseActivity
import com.github.denisidoro.reseau.viewbinder.ViewBinder
import com.github.denisidoro.reseau.viewmodel.ViewModel
import rx.subjects.PublishSubject

abstract class ViewController<A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        activity: A,
        protected val rootView: ViewGroup
) : ActivityController<A>(activity) {

    constructor(activity: A, @IdRes resourceId: Int) : this(activity, activity.findViewById(resourceId) as ViewGroup)
    constructor(activity: A) : this(activity, activity.rootView as ViewGroup)

    private val viewBinder: B by lazy { createViewBinder(rootView) { dispatch(it) } }
    private val viewModelSubject by lazy { PublishSubject.create<M>() }

    override fun onCreate() {
        super.onCreate()

        viewModelSubject
                .distinctUntilChanged()
                .doOnNext { viewBinder.bind(it) }
                .subscribe()
                .register()
    }

    abstract fun createViewBinder(rootView: ViewGroup, dispatch: (Any) -> Any): B

    override fun unsubscribe() {
        viewBinder.unsubscribe()
        super.unsubscribe()
    }

    fun emitViewModel(viewModel: M) {
        viewModelSubject.onNext(viewModel)
    }

}
