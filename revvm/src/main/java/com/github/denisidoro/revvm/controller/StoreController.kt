package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.redux.RxStore

interface StoreController: Controller {
    val store: RxStore<out Any>
}
