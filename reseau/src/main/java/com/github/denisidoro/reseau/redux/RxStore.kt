package com.github.denisidoro.reseau.redux

import redux.INIT
import redux.api.Reducer
import redux.api.Store
import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subjects.SerializedSubject
import rx.subscriptions.CompositeSubscription

class RxStore<S>(
        reducer: Reducer<S>,
        initialState: S,
        enhancer: Store.Enhancer<S>): Store<S> {

    private val subscription = CompositeSubscription()

    private var currentState: S = initialState

    private val creator = Store.Creator<S> { reducer, initialState ->
        object : Store<S> {
            private var reducer = reducer

            override fun dispatch(action: Any): Any {
                dispatcher.onNext(action)
                return action
            }

            override fun getState(): S = currentState

            override fun subscribe(subscriber: Store.Subscriber): Store.Subscription =
                    Store.Subscription {
                        subscription.unsubscribe()
                    }

            override fun replaceReducer(reducer: Reducer<S>) {
                this.reducer = reducer
                dispatch(INIT)
            }
        }
    }

    val store: Store<S> = enhancer.enhance(creator).create(reducer, initialState)

    private val storeSubscription: Store.Subscription = store.subscribe {  }

    val observable: Observable<S>
    private val dispatcher = SerializedSubject<Any, Any>(BehaviorSubject.create<Any>())

    init {
        observable = dispatcher
                .scan(currentState, { currentState, action -> reducer.reduce(currentState, action) })
                .doOnNext { newState -> currentState = newState }
                .share()

        subscription.add(observable.subscribe())

        store.dispatch(INIT)
    }

    override fun dispatch(action: Any): Any = store.dispatch(action)

    override fun getState(): S = store.state

    override fun replaceReducer(reducer: Reducer<S>) = store.replaceReducer(reducer)

    override fun subscribe(subscriber: Store.Subscriber): Store.Subscription = store.subscribe(subscriber)

    fun unsubscribe() {
        subscription.unsubscribe()
        storeSubscription.unsubscribe()
    }

}
