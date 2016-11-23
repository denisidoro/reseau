package com.github.denisidoro.patternsample.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout

import com.github.denisidoro.patternsample.R

class CounterView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        LayoutInflater.from(context).inflate(R.layout.counter, this, true)
    }

}
