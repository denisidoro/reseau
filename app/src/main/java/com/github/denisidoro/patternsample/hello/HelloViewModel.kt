package com.github.denisidoro.patternsample.hello

import com.github.denisidoro.revvm.viewmodel.ViewModel

class HelloViewModel(s: HelloState) : ViewModel {
    val text = s.i.toString()
}
