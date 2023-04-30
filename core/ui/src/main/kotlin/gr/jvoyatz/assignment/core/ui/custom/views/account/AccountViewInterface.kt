package gr.jvoyatz.assignment.core.ui.custom.views.account

import gr.jvoyatz.assignment.core.common.utils.Constants

interface AccountViewInterface {
    fun setAccountName(name: String? = Constants.DASH_WITH_SPACE)

    fun setAccountBalance(balance: String, currency: String)

    fun setAccountFavorite(favorite: Boolean)

    fun setAccountFavoriteClickListener(clickHandler: () -> Unit)
}