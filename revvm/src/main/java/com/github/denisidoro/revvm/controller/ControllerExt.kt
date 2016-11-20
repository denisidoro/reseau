package com.github.denisidoro.revvm.controller

fun Controller.propagate(f: (Controller) -> Unit) = children.forEach(f)
fun Controller.invokeAndPropagate(f: (Controller) -> Unit) = listOf(this).plus(children).forEach(f)
