package com.github.denisidoro.patternsample.hello

import com.github.denisidoro.revvm.viewmodel.ViewModel

class CounterViewModel(s: CounterState) : ViewModel {
    val text = s.i.toString()
}
