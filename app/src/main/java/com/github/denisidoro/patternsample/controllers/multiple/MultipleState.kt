package com.github.denisidoro.patternsample.controllers.multiple

import com.github.denisidoro.patternsample.controllers.counter.CounterState

class MultipleState(
        val counter0: CounterState,
        val counter1: CounterState)