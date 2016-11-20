package com.github.denisidoro.revvm.redux

interface Reducer<S> {
    val reduce: (S, Any) -> S
}

class SimpleReducer<S>(override val reduce: (S, Any) -> S): Reducer<S>
