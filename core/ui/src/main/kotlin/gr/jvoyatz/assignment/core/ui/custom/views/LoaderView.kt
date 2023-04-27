package gr.jvoyatz.assignment.core.ui.custom.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import gr.jvoyatz.assignment.core.ui.databinding.CustomLoaderViewBinding
import gr.jvoyatz.assignment.core.ui.utils.loadingAnimation
import gr.jvoyatz.assignment.core.ui.utils.showWithAnimation
import timber.log.Timber

class LoaderView(
    context: Context,
    attrs: AttributeSet? = null
): ConstraintLayout(context, attrs), ILoaderView {

    private val binding: CustomLoaderViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomLoaderViewBinding.inflate(inflater, this, true)

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams = lp

        loadingAnimation()
    }
    override fun showLoading() {
        binding.apply {
            loading.showWithAnimation()
            noResults.isVisible = false
            error.isVisible = false
            retry.isVisible = false
        }
    }

    override fun showError() {
        binding.apply {
            retry.showWithAnimation()
            error.showWithAnimation()
            loading.isVisible = false
            noResults.isVisible = false
        }
    }

    override fun showNoData() {
        binding.apply {
            retry.showWithAnimation()
            noResults.showWithAnimation()
            loading.isVisible = false
            error.isVisible = false
        }
    }
}