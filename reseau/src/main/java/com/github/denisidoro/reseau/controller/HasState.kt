package com.github.denisidoro.reseau.controller

import rx.Observable

interface HasState<S> {
    var state: S
    val stateObservable: Observable<S>
}
