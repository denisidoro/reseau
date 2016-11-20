package com.github.denisidoro.patternsample.hello

import com.github.denisidoro.revvm.viewbinder.ViewBinder
import kotlinx.android.synthetic.main.content_hello.view.*

class CounterViewBinder(activity: CounterActivity, dispatch: (Any) -> Any) : ViewBinder<CounterViewModel>(activity, dispatch) {

    init {
        with(root) {
            minusBT.setOnClickListener { dispatch(CounterActions.MINUS) }
            plusBT.setOnClickListener { dispatch(CounterActions.PLUS) }
        }
    }

    override fun bind(viewModel: CounterViewModel) {
        with(root) {
            countTV.text = viewModel.text
        }
    }

}