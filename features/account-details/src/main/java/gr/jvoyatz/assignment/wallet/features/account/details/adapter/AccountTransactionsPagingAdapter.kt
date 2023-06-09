package gr.jvoyatz.assignment.wallet.features.account.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountTransactionUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.models.TransactionUI
import gr.jvoyatz.assignment.wallet.features.account.details.databinding.AccountDetailsItemDateBinding
import gr.jvoyatz.assignment.wallet.features.account.details.databinding.AccountDetailsItemTransactionBinding
import gr.jvoyatz.assignment.wallet.features.account.details.databinding.ProgressbarBinding

class AccountTransactionsPagingAdapter :
    ListAdapter<TransactionUI, RecyclerView.ViewHolder>(AccountTransactionDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is TransactionUI.Holder -> ITEM
            is TransactionUI.Date -> HEADER
            is TransactionUI.Loading -> LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM -> {
                AccountDetailsItemTransactionBinding.inflate(inflater, parent, false).run {
                    ViewHolder(this)
                }
            }

            HEADER -> {
                AccountDetailsItemDateBinding.inflate(inflater, parent, false).run {
                    HeaderViewHolder(this)
                }
            }
            else -> {
                ProgressbarBinding.inflate(inflater, parent, false).run {
                    ProgressViewHolder(this)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item is TransactionUI.Holder && holder is ViewHolder) {
            holder.bind(item.transactionUiModel)
        } else if (item is TransactionUI.Date && holder is HeaderViewHolder) {
            holder.bind(item)
        }
    }

    inner class ViewHolder(private val binding: AccountDetailsItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccountTransactionUiModel) = kotlin.run {
            AccountTransactionViewMapper(
                binding.root.context,
                binding,
                item
            )
            Unit
        }
    }

    inner class ProgressViewHolder(view: ProgressbarBinding): RecyclerView.ViewHolder(view.root)

    fun showLoading(){
        val list = currentList.toMutableList()
        list.add(TransactionUI.Loading)
        submitList(list)
    }

    fun isLoading(): Boolean = !currentList.isEmpty() && currentList.last() is TransactionUI.Loading

    inner class HeaderViewHolder(private val binding: AccountDetailsItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransactionUI.Date) {
            binding.root.text = item.date
        }
    }

    companion object {
        internal const val HEADER = 1
        internal const val ITEM = 2
        internal const val LOADING = 3
    }
}