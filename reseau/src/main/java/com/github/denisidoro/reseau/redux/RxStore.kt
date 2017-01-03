package com.github.denisidoro.reseau.redux

import redux.api.Reducer
import redux.api.Store
import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subjects.SerializedSubject
import rx.subscriptions.CompositeSubscription

class RxStore<S>(
        private var currentState: S,
        private var reducer: Reducer<S>)
    : Store<S> {

    private val subscription = CompositeSubscription()
    private val storeSubscription: Store.Subscription = subscribe { }

    override fun replaceReducer(reducer: Reducer<S>) {
        this.reducer = reducer
    }

    val observable: Observable<S>
    private val dispatcher = SerializedSubject<Any, Any>(BehaviorSubject.create<Any>())

    init {
        observable = dispatcher
                .scan(currentState, { currentState, action -> reducer.reduce(currentState, action) })
                .doOnNext { newState -> currentState = newState }
                .share()

        subscription.add(observable.subscribe())
    }

    override fun dispatch(action: Any): Any {
        dispatcher.onNext(action)
        return action
    }

    override fun subscribe(subscriber: Store.Subscriber): Store.Subscription =
            Store.Subscription {
                subscription.unsubscribe()
            }

    override fun getState(): S = currentState

    fun unsubscribe() = storeSubscription.unsubscribe()

}
