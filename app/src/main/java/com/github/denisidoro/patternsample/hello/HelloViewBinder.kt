package com.github.denisidoro.patternsample.hello

import com.github.denisidoro.revvm.viewbinder.ViewBinder
import kotlinx.android.synthetic.main.content_hello.view.*

class HelloViewBinder(activity: HelloActivity, dispatch: (Any) -> Any) : ViewBinder<HelloViewModel>(activity, dispatch) {

    init {
        var i = 0
        i++
        with(root) {
            minusBT.setOnClickListener { dispatch(HelloActions.MINUS) }
            plusBT.setOnClickListener { dispatch(HelloActions.PLUS) }
        }
    }

    override fun bind(viewModel: HelloViewModel) {
        with(root) {
            countTV.text = viewModel.text
        }
    }

}