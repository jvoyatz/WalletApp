package gr.jvoyatz.assignment.wallet.accounts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.jvoyatz.assignment.core.ui.custom.views.account.AccountViewInterface
import gr.jvoyatz.assignment.wallet.accounts.databinding.AccountsListItemBinding
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountUiModel

/**
 * ListAdapter used to show the user's account as return from the web service
 */
class AccountListAdapter(
    val onAccountClicked: (account: AccountUiModel) -> Unit
): ListAdapter<AccountUiModel, AccountListAdapter.ViewHolder>(AccountDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AccountsListItemBinding.inflate(inflater, parent, false)
        return with(binding.root){->
            ViewHolder(this) { position ->
                with(getItem(position)) {
                    onAccountClicked(this)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        with(getItem(position)) {
            holder.bind(this)
        }

    inner class ViewHolder(
        private val view: View,
        onAccountViewClickListener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                onAccountViewClickListener(bindingAdapterPosition)
            }
        }

        fun bind(account: AccountUiModel) {
            AccountViewMapper(
                view.context,
                view as AccountViewInterface,
                account
            )
        }
    }
}