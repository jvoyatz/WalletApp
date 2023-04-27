package gr.jvoyatz.afse.wallet.features.accounts.data.internal

import gr.jvoyatz.afse.core.common.utils.StringConstants
import gr.jvoyatz.afse.core.common.utils.mapList
import gr.jvoyatz.afse.core.database.entities.AccountEntity
import gr.jvoyatz.afse.wallet.common.android.domain.models.AccountType
import gr.jvoyatz.afse.wallet.features.accounts.domain.models.Account

/**
 * Maps an [Account] domain into [AccountEntity] and vice-versa.
 * Also it offers a way to map a list of [AccountEntity] objects into corresponding domain [Account] model
 */
internal object AccountMappers {
    fun Account.toAccountEntity() = AccountEntity(
        id = id ?: StringConstants.EMPTY,
        accountNumber = accountNumber,
        accountNickname = accountNickname,
        accountType = accountType.type,
        balance = balance,
        currencyCode = currencyCode
    )

    private fun AccountEntity.toAccount() = Account(
        id = id,
        accountNumber = accountNumber,
        accountNickname = accountNickname,
        accountType = AccountType[accountType],
        balance = balance,
        currencyCode = currencyCode
    )

    fun List<AccountEntity>.toAccounts() = this.mapList { it.toAccount() }
}