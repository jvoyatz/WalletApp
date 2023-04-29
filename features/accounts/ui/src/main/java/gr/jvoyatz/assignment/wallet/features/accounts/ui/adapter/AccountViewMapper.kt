package gr.jvoyatz.assignment.wallet.features.accounts.ui.adapter

import android.content.Context
import gr.jvoyatz.assignment.core.ui.custom.views.account.AccountViewInterface
import gr.jvoyatz.assignment.wallet.features.accounts.ui.model.AccountUi

/**
 * Maps the data received in [AccountListAdapter.ViewHolder.bind] to the corresponding
 * [AccountView] through the contract [AccountViewInterface]
 */
class AccountViewMapper(
    private val context: Context,
    private val view: AccountViewInterface,
    private val account: AccountUi,
) {
    init {
        setAccountName()
        setAccountBalance()
    }
    private fun setAccountName(){
        account.accountNickname?.let {
            view.setAccountName(it)
        } ?: run {
            val name = context.getString(gr.jvoyatz.assignment.core.ui.R.string.not_provided)
            view.setAccountName(name)
        }
    }

    private fun setAccountBalance() {
        view.setAccountBalance(account.balance, account.currencyCode)
    }
}