package com.github.denisidoro.revvm.redux

class Reducer<S>(val reduce: (S, Any) -> S)
