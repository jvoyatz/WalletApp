package gr.jvoyatz.assignment.wallet.features.accounts.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import gr.jvoyatz.assignment.wallet.features.accounts.domain.models.Account

/**
 * Calculates the diff between two Account items in the given list
 */
class AccountDiffCallback: DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Account, newItem: Account) = oldItem == newItem
}