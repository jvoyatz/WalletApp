package gr.jvoyatz.assignment.wallet.common.android.ui.models

import android.os.Parcelable
import gr.jvoyatz.assignment.wallet.domain.models.AccountType
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountUiModel(
    val id: String,
    val accountNickname: String?,
    val accountNumber: Int,
    val accountType: AccountType,
    val balance: String,
    val currencyCode: String,
    var isFavorite: Boolean = false,
    val details: AccountDetailsUiModel ?= null,
    val transactions: List<TransactionUI> ?= null,
    var paging: PagingUiModel? = null
) : Parcelable