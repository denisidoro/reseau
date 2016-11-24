package com.github.denisidoro.reseau.redux

open class Reducer<S>(val reduce: (S, Any) -> S)
