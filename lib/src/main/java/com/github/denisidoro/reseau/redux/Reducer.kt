package com.github.denisidoro.reseau.redux

class Reducer<S>(val reduce: (S, Any) -> S)
