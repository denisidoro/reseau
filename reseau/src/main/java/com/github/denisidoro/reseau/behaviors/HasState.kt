package com.github.denisidoro.reseau.behaviors

import rx.Observable

interface HasState<S> {
    var state: S
    val stateObservable: Observable<S>
}
