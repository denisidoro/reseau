package com.github.denisidoro.revvm.activity

import android.R
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {

    val rootView by lazy { findViewById(R.id.content).rootView }

}

