package com.github.denisidoro.revvm.controller

import rx.Observable

interface Controller {

    var parent: Controller?
    val children: List<Controller>
    val observable: Observable<out Any>

    fun dispatchLocal(action: Any): Any

    fun getRoot(): Controller

    fun dispatchRoot(action: Any): Any

    fun dispatch(action: Any): Any

    fun unsubscribe() {
    }

}
