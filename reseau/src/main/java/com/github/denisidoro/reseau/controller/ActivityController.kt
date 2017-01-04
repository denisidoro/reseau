package com.github.denisidoro.reseau.controller

import com.github.denisidoro.reseau.activity.BaseActivity
import java.lang.ref.WeakReference

abstract class ActivityController<A : BaseActivity>(
        private val activityRef: WeakReference<A>,
        controller: Controller)
    : Controller by controller {

    constructor(activity: A) : this(WeakReference(activity), SimpleController())

    var activity: A = activityRef.get()
        get() = activityRef.get()

}
