package gr.jvoyatz.assignment.core.ui.custom.views.common

/**
 * Contract for the custom view [LoaderView]
 */
interface LoaderViewInterview {
    fun showLoading()

    fun showError()

    fun showNoData()

    fun setRetryListener(clickHandler: () -> Unit)
}