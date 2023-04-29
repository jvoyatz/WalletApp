package gr.jvoyatz.assignment.wallet.accounts.model

import android.os.Parcelable
import gr.jvoyatz.assignment.wallet.common.android.domain.models.AccountType
import kotlinx.parcelize.Parcelize

@Parcelize
class AccountUi(
    val id: String,
    val accountNickname: String?,
    val accountNumber: Int,
    val accountType: AccountType,
    val balance: String,
    val currencyCode: String,
    val isFavorite: Boolean = false
) : Parcelable