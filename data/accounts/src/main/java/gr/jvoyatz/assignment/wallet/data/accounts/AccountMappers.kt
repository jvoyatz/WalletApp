package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.core.common.utils.mapList
import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import gr.jvoyatz.assignment.wallet.core.api.models.AccountDetailsDto
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.assignment.wallet.core.api.models.AccountTransactionsDto
import gr.jvoyatz.assignment.wallet.core.api.models.PagingDto
import gr.jvoyatz.assignment.wallet.core.api.models.TransactionRaw
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.AccountDetails
import gr.jvoyatz.assignment.wallet.domain.models.AccountType
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions
import gr.jvoyatz.assignment.wallet.domain.models.Paging
import gr.jvoyatz.assignment.wallet.domain.models.Transaction


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

    fun AccountDetailsDto.toDomain() = AccountDetails(
        beneficiaries = this.beneficiaries,
        branch = this.branch,
        openedDate = this.openedDate,
        productName = this.productName
    )

    fun AccountTransactionsDto.toPagedAccountTransactions(currencySymbol: String? = null) = PagedTransactions(
        paging = this.paging.toDomain(),
        transactions = this.transactions.mapList { it.toDomain(currencySymbol ?: "") }
    )

    fun PagingDto.toDomain() = Paging(
        currentPage = this.currentPage,
        pagesCount = pagesCount,
        totalItems = totalItems
    )

    fun TransactionRaw.toDomain(currencySymbol: String) = Transaction(
        date,
        description,
        id,
        isDebit,
        "$transactionAmount$currencySymbol",
        transactionType
    )
}