package com.github.denisidoro.reseau.redux

interface Store<S> {
    val state: S
    var dispatch: (action: Any) -> Any
    fun replaceReducer(reducer: Reducer<S>)
}
