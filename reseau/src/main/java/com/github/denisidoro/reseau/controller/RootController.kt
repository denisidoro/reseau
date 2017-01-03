package com.github.denisidoro.reseau.controller

import com.github.denisidoro.reseau.behaviors.HasState
import rx.Observable
import java.util.*

abstract class RootController : Controller(), HasState<HashMap<String, Any>> {

    override val stateObservable: Observable<HashMap<String, Any>> by lazy {
        val n = statefulNodes()
        Observable.combineLatest(
                n.map { it.castAndGetStateObservable() },
                { states ->
                    hashMapOf(*n.map { it.name }
                            .zip(states)
                            .toTypedArray()) })
    }

    override var state: HashMap<String, Any> = hashMapOf()
        get() = hashMapOf(*statefulNodes()
                .map { Pair(it.name, it.castAndGetState()) }
                .toTypedArray())

    private fun statefulNodes() = nodes().minus(this).filter { it is HasState<*> }

    private fun Controller.nodes(): List<Controller> =
            if (children.isEmpty()) listOf(this)
            else listOf(this).plus(children.flatMap { it.nodes() })

    private fun Controller.castAndGetStateObservable() = (this as HasState<Any>).stateObservable
    //private fun Controller.castAndGetState() = (this as HasState<Any>).state

}