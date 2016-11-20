package com.github.denisidoro.revvm.activity

import android.support.v7.app.AppCompatActivity
import android.view.View

abstract class BaseActivity: AppCompatActivity() {

    val rootView: View by lazy { findViewById(android.R.id.content).rootView }



}

