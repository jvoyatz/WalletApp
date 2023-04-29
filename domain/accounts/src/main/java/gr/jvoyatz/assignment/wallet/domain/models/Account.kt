package gr.jvoyatz.assignment.wallet.domain.models

import gr.jvoyatz.assignment.wallet.common.android.domain.models.AccountType

/**
 * Account domain model
 */
data class Account(
    val id: String,
    val accountNickname: String?,
    val accountNumber: Int,
    val accountType: AccountType,
    val balance: String,
    val currencyCode: String,
    val isFavorite: Boolean = false
){}