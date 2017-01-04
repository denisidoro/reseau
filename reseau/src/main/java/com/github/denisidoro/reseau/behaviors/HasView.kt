package com.github.denisidoro.reseau.behaviors

import android.view.ViewGroup
import com.github.denisidoro.reseau.viewbinder.ViewBinder
import com.github.denisidoro.reseau.viewmodel.ViewModel

interface HasView<in M : ViewModel, out B : ViewBinder<M>> {
    fun createViewBinder(rootView: ViewGroup, dispatch: (Any) -> Any): B
    fun emitViewModel(viewModel: M): Unit
}