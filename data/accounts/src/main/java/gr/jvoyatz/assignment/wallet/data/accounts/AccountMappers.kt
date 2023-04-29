package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.core.common.utils.ConstantsString
import gr.jvoyatz.assignment.core.common.utils.mapList
import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import gr.jvoyatz.assignment.wallet.common.android.domain.models.AccountType
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.assignment.wallet.core.api.models.AccountsDto
import gr.jvoyatz.assignment.wallet.domain.models.Account


/**
 * Maps an [Account] domain into [AccountEntity] and vice-versa.
 * Also it offers a way to map a list of [AccountEntity] objects into corresponding domain [Account] model
 */
internal object AccountMappers {
    fun Account.toAccountEntity() = AccountEntity(
        id = id,
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

    fun List<AccountEntity>.entitiesToAccounts() = this.mapList { it.toAccount() }

    fun AccountRaw.toAccount() = Account(
        id = id,
        accountNumber = accountNumber,
        accountNickname = accountNickname,
        accountType = AccountType[accountType],
        balance = balance,
        currencyCode = currencyCode
    )
    fun List<AccountRaw>.dtoToAccounts() = this.mapList { it.toAccount() }
}