package gr.jvoyatz.afse.core.ui

/**
 * Contract for the custom view [LoaderView]
 */
interface ILoaderView {
    fun showLoading()

    fun showError()

    fun showNoData()
}