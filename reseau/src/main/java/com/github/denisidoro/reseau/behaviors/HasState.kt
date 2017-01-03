package com.github.denisidoro.reseau.behaviors

import rx.Observable

interface HasState<S> {
    fun getState(): S
    val stateObservable: Observable<S>
}
