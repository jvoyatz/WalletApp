package gr.jvoyatz.assignment.core.ui.custom.views

/**
 * Contract for the custom view [LoaderView]
 */
interface ILoaderView {
    fun showLoading()

    fun showError()

    fun showNoData()
}