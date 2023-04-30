package gr.jvoyatz.assignment.wallet.features.account.details.adapter

import android.content.Context
import gr.jvoyatz.assignment.core.ui.R
import gr.jvoyatz.assignment.core.ui.utils.getAttrColor
import gr.jvoyatz.assignment.core.ui.utils.rightDrawable
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountTransactionUiModel
import gr.jvoyatz.assignment.wallet.features.account.details.databinding.AccountDetailsItemTransactionBinding
import gr.jvoyatz.assignment.core.ui.R as UI_R
class AccountTransactionViewMapper(
    private val context: Context,
    private val binding: AccountDetailsItemTransactionBinding,
    private val model: AccountTransactionUiModel
) {
    init {
        with(binding){
            transactionType.text = model.transactionType
            transactionDescription.text = model.description ?: root.context.getString(R.string.not_provided_description)

            val icon = if(model.isDebit) UI_R.drawable.ic_arrow_right else UI_R.drawable.ic_arrow_left
            val color = binding.root.getAttrColor {
                if(model.isDebit) android.R.attr.colorError
                else R.attr.colorSuccess
            }
            with(binding.transactionAmount){
                text = model.transactionAmount
                rightDrawable(icon)
                compoundDrawables[2]?.setTint(color)
            }
        }
    }
}