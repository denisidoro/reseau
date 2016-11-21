package com.github.denisidoro.revvm.controller

import com.github.denisidoro.revvm.activity.BaseActivity
import java.lang.ref.WeakReference

abstract class ActivityController<A : BaseActivity>(private val activityRef: WeakReference<A>) : SimpleController() {

    constructor(activity: A) : this(WeakReference(activity))

    fun getActivity(): A = activityRef.get()

}
