package com.github.denisidoro.revvm.redux

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

class RxStore<S>(
        override var state: S,
        private var reducer: Reducer<S>) : Store<S> {

    override fun replaceReducer(reducer: Reducer<S>) {
        this.reducer = reducer
    }

    val observable: Observable<S>
    private val dispatcher = SerializedSubject<Any, Any>(PublishSubject.create<Any>())

    init {
        observable = dispatcher
                .scan(state, { state, action -> reducer.reduce(state, action) })
                .doOnNext { newState -> state = newState }
                .share()

        observable.subscribe()
    }

    override var dispatch: (action: Any) -> Any = { action ->
        dispatcher.onNext(action)
        action
    }

}
