package com.github.denisidoro.revvm.redux

interface Reducer<S> {
    fun reduce(state: S, action: Any): S
}