package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.exception.ControllerNotFoundException
import java.util.*

// temp
abstract class HolderController: Controller() {

    fun getObservablePairs(): HashMap<String, Any> =
            hashMapOf(*nodesBelow()
                    .filter { it is HasState<*> }
                    .map { Pair(it.name, (it as HasState<*>).state!!) }
                    .toTypedArray())

    fun findByName(name: String): Controller =
            nodesBelow().filter { it.name == name }.firstOrNull() ?: throw ControllerNotFoundException()


}