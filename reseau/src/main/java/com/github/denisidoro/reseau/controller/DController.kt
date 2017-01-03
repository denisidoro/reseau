package com.github.denisidoro.reseau.controller

import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.view.ViewGroup
import com.github.denisidoro.reseau.activity.BaseActivity
import com.github.denisidoro.reseau.behaviors.HasActivityLifecycle
import com.github.denisidoro.reseau.behaviors.HasState
import com.github.denisidoro.reseau.redux.RxStore
import com.github.denisidoro.reseau.viewbinder.ViewBinder
import com.github.denisidoro.reseau.viewmodel.ViewModel
import redux.api.Reducer
import redux.api.Store
import rx.Observable
import rx.subjects.PublishSubject
import java.lang.ref.WeakReference

interface HasStore<S : Any> : HasState<S> {
    val store: RxStore<S>
}

abstract class StoreBehavior<S : Any> : HasStore<S> {

    override val store: RxStore<S> by lazy { RxStore(getReducer(), getInitialState(), getEnhancer()) }

    abstract fun getReducer(): Reducer<S>

    abstract fun getInitialState(): S

    open fun getEnhancer(): Store.Enhancer<S> = Store.Enhancer { it }

    override fun getState(): S = store.state

    override val stateObservable: Observable<S> by lazy { store.observable.startWith(store.state) }

}

interface HasActivity<out A : BaseActivity> {
    fun getActivity(): A
}

abstract class ActivityBehavior<A : BaseActivity>(
        private val activityRef: WeakReference<A>) : HasActivity<A> {

    constructor(activity: A) : this(WeakReference(activity))

    override fun getActivity(): A = activityRef.get()
}


abstract class ViewBehavior<A : BaseActivity, M : ViewModel, B : ViewBinder<M>>(
        protected val rootView: ViewGroup,
        ab: ActivityBehavior<A>
) : HasActivity<A> by ab {

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


class StoreController<S : Any>(sb: StoreBehavior<S>) : Controller(), HasStore<S> by sb {

    override fun dispatchLocal(action: Any) = store.dispatch(action)

}

class StoreActivityController<S : Any, out A : BaseActivity>(
        sb: StoreBehavior<S>,
        ab: ActivityBehavior<A>
) : Controller(), HasStore<S> by sb, HasActivity<A> by ab {

    override fun dispatchLocal(action: Any) = store.dispatch(action)
}
