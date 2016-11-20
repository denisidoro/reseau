package com.github.denisidoro.patternsample.controllers.counter

import com.github.denisidoro.revvm.activity.BaseActivity
import com.github.denisidoro.revvm.viewbinder.ViewBinder
import kotlinx.android.synthetic.main.content_hello.view.*

class CounterViewBinder(activity: BaseActivity, dispatch: (Any) -> Any) : ViewBinder<CounterViewModel>(activity, dispatch) {

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