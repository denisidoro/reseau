package com.github.denisidoro.patternsample.controllers.counter

import android.view.ViewGroup
import com.github.denisidoro.reseau.viewbinder.ViewBinder
import kotlinx.android.synthetic.main.counter.view.*

class CounterViewBinder(
        viewGroup: ViewGroup,
        dispatch: (Any) -> Any)
    : ViewBinder<CounterViewModel>(viewGroup, dispatch) {

    init {
        with(root) {
            minusBT.setOnClickListener { dispatch(CounterActions.DECREMENT) }
            plusBT.setOnClickListener { dispatch(CounterActions.INCREMENT) }
        }
    }

    override fun bind(viewModel: CounterViewModel) {
        with(root) {
            countTV.text = viewModel.text
        }
    }

}