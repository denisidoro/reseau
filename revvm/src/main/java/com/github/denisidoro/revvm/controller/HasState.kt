package com.github.denisidoro.revvm.controller

import rx.Observable

interface HasState<S> {
    var state: S
    val stateObservable: Observable<S>
}
