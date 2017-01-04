package com.github.denisidoro.reseau.behaviors

import rx.Observable

interface HasState<S: Any> {
    fun getState(): S
    val stateObservable: Observable<S>
}
