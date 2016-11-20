package com.github.denisidoro.revvm.controller

fun Controller.propagate(f: (Controller) -> Unit) = children.forEach(f)
fun Controller.invokeForAll(f: (Controller) -> Unit) = nodesBelow().plus(children).forEach(f)
