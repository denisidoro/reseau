package com.github.denisidoro.patternsample.controllers.multiple

import com.github.denisidoro.patternsample.R
import com.github.denisidoro.patternsample.activities.MultipleActivity
import com.github.denisidoro.patternsample.controllers.counter.CounterController
import com.github.denisidoro.revvm.controller.Controller
import com.github.denisidoro.revvm.controller.SimpleController
import com.github.denisidoro.revvm.controller.StoreController
import rx.Observable
import java.util.*

class MultipleController(activity: MultipleActivity) : SimpleController() {

    val counter0 = CounterController(activity, R.id.counter0)
    val counter1 = CounterController(activity, R.id.counter1)

    override val children: List<Controller> =
            listOf(counter0, counter1)

    val o: HashMap<String, Observable<*>> = hashMapOf(*nodesBelow().map { Pair(it.name, it.observable) }.toTypedArray())
    val s: HashMap<String, Any> =
            hashMapOf(
                    *nodesBelow()
                            .filter { it is StoreController<*> }
                            .map { Pair(it.name, (it as StoreController<*>).store.state!!) }
                            .toTypedArray())

}
