package gr.jvoyatz.assignment.core.ui.custom.views.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import gr.jvoyatz.assignment.core.ui.databinding.CustomLoaderViewBinding
import gr.jvoyatz.assignment.core.ui.utils.fromTopAnimation
import gr.jvoyatz.assignment.core.ui.utils.fromBottomAnimation

class LoaderView(
    context: Context,
    attrs: AttributeSet? = null
): ConstraintLayout(context, attrs), LoaderViewInterview {

    private val binding: CustomLoaderViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomLoaderViewBinding.inflate(inflater, this, true)

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams = lp

        //fromTopAnimation()
    }
    override fun showLoading() {
        binding.apply {
            loading.fromBottomAnimation()
            noResults.isVisible = false
            error.isVisible = false
            retry.isVisible = false
        }
    }

    override fun showError() {
        binding.apply {
            retry.fromBottomAnimation()
            error.fromBottomAnimation()
            loading.isVisible = false
            noResults.isVisible = false
        }
    }

    override fun showNoData() {
        binding.apply {
            retry.fromBottomAnimation()
            noResults.fromBottomAnimation()
            loading.isVisible = false
            error.isVisible = false
        }
    }

    override fun setRetryListener(clickHandler: () -> Unit) {
        binding.retry.setOnClickListener {
            clickHandler
        }
    }
}