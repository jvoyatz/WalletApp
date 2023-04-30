package gr.jvoyatz.assignment.wallet.features.account.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountDetailsUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountTransactionUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.models.TransactionUI
import gr.jvoyatz.assignment.wallet.features.account.details.R
import gr.jvoyatz.assignment.wallet.features.account.details.databinding.AccountDetailsItemDateBinding
import gr.jvoyatz.assignment.wallet.features.account.details.databinding.AccountDetailsItemTransactionBinding
import java.lang.IllegalStateException

class AccountTransactionsPagingAdapter :
    ListAdapter<TransactionUI, RecyclerView.ViewHolder>(
        AccountTransactionDiffCallback()
    ) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is TransactionUI.Holder -> ITEM
            is TransactionUI.Date -> HEADER
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
                throw IllegalStateException("unknown view type, eager crash to detect errors")
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

    inner class HeaderViewHolder(private val binding: AccountDetailsItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransactionUI.Date) {
            binding.root.text = item.date
        }
    }

    companion object {
        internal const val HEADER = 1
        internal const val ITEM = 2
    }
}