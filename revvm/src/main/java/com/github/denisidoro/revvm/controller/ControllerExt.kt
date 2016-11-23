package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.exception.ObservableCastException
import com.github.denisidoro.revvm.exception.RevvmException
import com.github.denisidoro.revvm.exception.RootCastException
import rx.Observable

inline fun <reified S> Controller.getGraphStateObservable(name: String): Observable<S> {
    try {
        val r = getRoot() as? HolderController ?: throw RootCastException()
        val c = r.findByName(name)
        val s = c as? HasState<S> ?: throw ObservableCastException()
        return s.stateObservable
    } catch (e: RevvmException) {
        return Observable.error(e)
    }
}