package gr.jvoyatz.assignment.wallet.features.accounts.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import gr.jvoyatz.assignment.wallet.features.accounts.domain.models.Account
import gr.jvoyatz.assignment.wallet.features.accounts.ui.model.AccountUi

/**
 * Calculates the diff between two Account items in the given list
 */
class AccountDiffCallback: DiffUtil.ItemCallback<AccountUi>() {
    override fun areItemsTheSame(oldItem: AccountUi, newItem: AccountUi) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AccountUi, newItem: AccountUi) = oldItem == newItem
}