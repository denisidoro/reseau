package com.github.denisidoro.revvm.controller

import android.support.annotation.IdRes
import android.view.ViewGroup
import com.github.denisidoro.revvm.activity.BaseActivity
import com.github.denisidoro.revvm.viewbinder.ViewBinder
import com.github.denisidoro.revvm.viewmodel.ViewModel
import rx.subjects.PublishSubject
import java.lang.ref.WeakReference

abstract class LegoController<A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        activityRef: WeakReference<A>,
        protected val root: ViewGroup
) : ActivityController<A>(activityRef) {

    constructor(activity: A, @IdRes resourceId: Int) : this(activity, activity.findViewById(resourceId) as ViewGroup)
    constructor(activity: A, root: ViewGroup) : this(WeakReference<A>(activity), root)
    constructor(activity: A) : this(activity, activity.rootView as ViewGroup)

    private val viewBinder: B by lazy { createViewBinder(root) { dispatchRoot(it) } }
    private val viewModelSubject by lazy { PublishSubject.create<M>() }

    abstract fun createViewBinder(root: ViewGroup, dispatch: (Any) -> Any): B

    override fun onCreate() {
        super.onCreate()

        viewModelSubject
                .distinctUntilChanged()
                .doOnNext { viewBinder.bind(it) }
                .subscribe()
                .register()
    }

    override fun unsubscribe() {
        viewBinder.unsubscribe()
        super.unsubscribe()
    }

    fun emitViewModel(viewModel: M) {
        viewModelSubject.onNext(viewModel)
    }

}
