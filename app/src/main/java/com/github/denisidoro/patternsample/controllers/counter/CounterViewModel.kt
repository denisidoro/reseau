package com.github.denisidoro.patternsample.controllers.counter

import com.github.denisidoro.revvm.viewmodel.ViewModel

class CounterViewModel(s: CounterState) : ViewModel {
    val text = s.i.toString()
}
