package com.github.denisidoro.reseau.controller

import com.github.denisidoro.reseau.exception.ObservableCastException
import com.github.denisidoro.reseau.exception.ReseauException
import com.github.denisidoro.reseau.exception.RootCastException
import rx.Observable

inline fun <reified S> Controller.getGraphStateObservable(name: String): Observable<S> {
    try {
        val r = getRoot() as? HolderController ?: throw RootCastException()
        val c = r.findByName(name)
        val s = c as? HasState<S> ?: throw ObservableCastException()
        return s.stateObservable
    } catch (e: ReseauException) {
        return Observable.error(e)
    }
}