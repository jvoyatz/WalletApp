package gr.jvoyatz.assignment.core.ui.custom.views.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import gr.jvoyatz.assignment.core.ui.databinding.LoaderViewBinding
import gr.jvoyatz.assignment.core.ui.utils.fromBottomAnimation
import gr.jvoyatz.assignment.core.ui.utils.show

class LoaderView(
    context: Context,
    attrs: AttributeSet? = null
): ConstraintLayout(context, attrs), LoaderViewInterview {

    private val binding: LoaderViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = LoaderViewBinding.inflate(inflater, this, true)

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams = lp

        //fromTopAnimation()
    }
    override fun showLoading() {
        binding.apply {
            show()
            loading.fromBottomAnimation()
            noResults.isVisible = false
            error.isVisible = false
            retry.isVisible = false
        }
    }

    override fun showError() {
        binding.apply {
            show()
            retry.fromBottomAnimation()
            error.fromBottomAnimation()
            loading.isVisible = false
            noResults.isVisible = false
        }
    }

    override fun showNoData() {
        binding.apply {
            show()
            retry.fromBottomAnimation()
            noResults.fromBottomAnimation()
            loading.isVisible = false
            error.isVisible = false
        }
    }

    override fun setRetryListener(clickHandler: () -> Unit) {
        binding.retry.setOnClickListener {
            clickHandler()
        }
    }
}