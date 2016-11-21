package com.github.denisidoro.patternsample.controllers.log

import android.view.ViewGroup
import com.github.denisidoro.revvm.viewbinder.ViewBinder
import kotlinx.android.synthetic.main.content_hello.view.*

class LogViewBinder(
        viewGroup: ViewGroup,
        dispatch: (Any) -> Any)
: ViewBinder<LogViewModel>(viewGroup, dispatch) {

    override fun bind(viewModel: LogViewModel) {
        with(root) {
            logTV.text = viewModel.text
        }
    }

}