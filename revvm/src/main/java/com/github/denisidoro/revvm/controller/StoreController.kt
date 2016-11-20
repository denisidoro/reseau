package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.redux.RxStore

interface StoreController<S>: Controller {
    val store: RxStore<S>
}
