package com.github.denisidoro.revvm.controller

import rx.Observable

interface Controller {

    val parent: Controller?
    val children: List<Controller>
    val observable: Observable<out Any>

    fun dispatchLocal(action: Any): Any

    fun getRoot(): Controller

    fun rootDispatch(action: Any): Any

    fun dispatch(action: Any): Any

    fun unsubscribe() {
    }

}
