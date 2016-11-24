package com.github.denisidoro.reseau.controller

import rx.Observable
import java.util.*

class MapController: Controller() {

    private fun getObservablePairs(): HashMap<String, Any> =
            hashMapOf(*nodesBelow()
                    .filter { it is HasState<*> }
                    .map { Pair(it.name, (it as HasState<*>).state!!) }
                    .toTypedArray())

    fun findByName(name: String): Controller =
            nodesBelow().filter { it.name == name }.first()

    inline fun <reified S> nodeStateObservable(name: String): Observable<S> =
            (findByName(name) as? HasState<S>)?.stateObservable ?: Observable.error(Exception("wrong"))

    /*
    val getGraphState: rx.Observable<Any> by lazy {
        val pairs = getObservablePairs()
        rx.Observable.combineLatest(pairs.map { it.value }, FuncN { it.first() })
    }
    */

}
