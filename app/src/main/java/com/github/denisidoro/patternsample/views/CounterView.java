package com.github.denisidoro.patternsample.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.github.denisidoro.patternsample.R;

public class CounterView extends LinearLayout {

    public CounterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        LayoutInflater.from(context).inflate(R.layout.counter, this, true);
    }

}
