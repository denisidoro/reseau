package com.github.denisidoro.patternsample.controllers.log

import com.github.denisidoro.patternsample.controllers.counter.CounterState
import com.github.denisidoro.reseau.viewmodel.ViewModel

class LogViewModel(s0: CounterState, s1: CounterState) : ViewModel {
    val text = "${s0.i}, ${s1.i}"
}
