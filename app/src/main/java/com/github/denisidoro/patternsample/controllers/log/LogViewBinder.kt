package com.github.denisidoro.patternsample.controllers.log

import android.view.ViewGroup
import com.github.denisidoro.reseau.viewbinder.ViewBinder
import kotlinx.android.synthetic.main.content_multiple.view.*

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