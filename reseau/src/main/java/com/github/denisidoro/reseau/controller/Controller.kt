package com.github.denisidoro.reseau.controller

import android.support.annotation.CallSuper
import com.github.denisidoro.reseau.exception.ObservableCastException
import com.github.denisidoro.reseau.exception.ParentAlreadySetException
import com.github.denisidoro.reseau.exception.ReseauException
import com.github.denisidoro.reseau.exception.RootCastException
import com.github.denisidoro.reseau.behaviors.HasActivityLifecycle
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription

interface Controller : HasActivityLifecycle {
    val name: String
    val children: List<Controller>
    var parent: Controller?
    val dispatchGroup: Int
    val root: Controller
    fun dispatchLocal(action: Any): Any
    fun dispatch(action: Any, caller: Controller, topDown: Boolean): Any
    fun unsubscribe()
}
