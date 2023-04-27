package gr.jvoyatz.afse.wallet.features.accounts.domain.models

import gr.jvoyatz.afse.wallet.common.android.domain.models.AccountType

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
)