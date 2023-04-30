package gr.jvoyatz.assignment.wallet.features.account.details.adapter

import androidx.recyclerview.widget.DiffUtil
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountTransactionUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.models.TransactionUI

class AccountTransactionDiffCallback: DiffUtil.ItemCallback<TransactionUI>() {
    override fun areItemsTheSame(
        oldItem: TransactionUI,
        newItem: TransactionUI
    ): Boolean {
        return if(oldItem is TransactionUI.Date && newItem is TransactionUI.Date){
            oldItem.date == newItem.date
        }else if (oldItem is TransactionUI.Holder && newItem is TransactionUI.Holder){
            oldItem.transactionUiModel.id == newItem.transactionUiModel.id
        } else{
            false
        }
    }

    override fun areContentsTheSame(
        oldItem: TransactionUI,
        newItem: TransactionUI
    ): Boolean {
        return oldItem == newItem
    }
}