package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.activity.BaseActivity
import com.github.denisidoro.revvm.viewbinder.ViewBinder
import com.github.denisidoro.revvm.viewmodel.ViewModel
import rx.Observable
import rx.subjects.PublishSubject
import java.lang.ref.WeakReference

abstract class LegoController<A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        private val activityRef: WeakReference<A>) : SimpleController() {

    constructor(activity: A) : this(WeakReference(activity))

    private val viewBinder: B by lazy { createViewBinder(getActivity()) { rootDispatch(it) } }
    private val viewModelSubject by lazy { PublishSubject.create<M>() }
    val viewModelObservable: Observable<M> = viewModelSubject.asObservable().share()

    abstract fun createViewBinder(activity: A, dispatch: (Any) -> Any): B

    override fun onCreate() {
        super.onCreate()

        viewModelSubject
                .distinctUntilChanged()
                .doOnNext { viewBinder.bind(it)  }
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

    fun getActivity(): A = activityRef.get()

}
