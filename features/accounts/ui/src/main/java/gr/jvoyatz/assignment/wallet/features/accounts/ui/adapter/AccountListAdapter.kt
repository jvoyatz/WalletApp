package gr.jvoyatz.assignment.wallet.features.accounts.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.jvoyatz.assignment.core.ui.custom.views.account.AccountView
import gr.jvoyatz.assignment.core.ui.custom.views.account.AccountViewInterface
import gr.jvoyatz.assignment.wallet.features.accounts.domain.models.Account
import gr.jvoyatz.assignment.wallet.features.accounts.ui.adapter.AccountListAdapter.ViewHolder
import gr.jvoyatz.assignment.wallet.features.accounts.ui.databinding.AccountsListItemBinding

/**
 * ListAdapter used to show the user's account as return from the web service
 */
class AccountListAdapter(
    val onAccountClicked: (account: Account) -> Unit
): ListAdapter<Account, ViewHolder>(AccountDiffCallback()) {

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

        fun bind(account: Account) {
            AccountViewMapper(
                view.context,
                view as AccountViewInterface,
                account
            )
        }
    }
}