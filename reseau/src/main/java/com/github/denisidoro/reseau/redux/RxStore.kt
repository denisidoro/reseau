package com.github.denisidoro.reseau.redux

import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subjects.SerializedSubject
import rx.subscriptions.CompositeSubscription

class RxStore<S>(
        override var state: S,
        private var reducer: Reducer<S>) : Store<S> {

    private val subscription = CompositeSubscription()

    override fun replaceReducer(reducer: Reducer<S>) {
        this.reducer = reducer
    }

    val observable: Observable<S>
    private val dispatcher = SerializedSubject<Any, Any>(BehaviorSubject.create<Any>())

    init {
        observable = dispatcher
                .scan(state, { state, action -> reducer.reduce(state, action) })
                .doOnNext { newState -> state = newState }
                .share()

        subscription.add(observable.subscribe())
    }

    override var dispatch: (action: Any) -> Any = { action ->
        dispatcher.onNext(action)
        action
    }

    fun unsubscribe() {
        subscription.unsubscribe()
    }

}
