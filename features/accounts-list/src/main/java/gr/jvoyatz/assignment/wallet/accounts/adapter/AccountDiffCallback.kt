package gr.jvoyatz.assignment.wallet.accounts.adapter

import androidx.recyclerview.widget.DiffUtil
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountUiModel

/**
 * Calculates the diff between two Account items in the given list
 */
class AccountDiffCallback: DiffUtil.ItemCallback<AccountUiModel>() {
    override fun areItemsTheSame(oldItem: AccountUiModel, newItem: AccountUiModel) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AccountUiModel, newItem: AccountUiModel) = oldItem == newItem
}