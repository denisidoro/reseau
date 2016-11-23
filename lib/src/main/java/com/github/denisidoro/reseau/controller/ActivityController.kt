package com.github.denisidoro.reseau.controller

import com.github.denisidoro.reseau.activity.BaseActivity
import java.lang.ref.WeakReference

abstract class ActivityController<A : BaseActivity>(private val activityRef: WeakReference<A>) : Controller() {

    constructor(activity: A) : this(WeakReference(activity))

    fun getActivity(): A = activityRef.get()

}
