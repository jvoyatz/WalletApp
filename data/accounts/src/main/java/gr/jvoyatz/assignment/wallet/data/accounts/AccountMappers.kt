package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.core.common.utils.Constants
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
import timber.log.Timber


/**
 * Maps an [Account] domain into [AccountEntity] and vice-versa.
 * Also it offers a way to map a list of [AccountEntity] objects into corresponding domain [Account] model
 */
@Suppress("unused")
internal object AccountMappers {
    fun Account.toAccountEntity() = AccountEntity(
        id = id,
        accountNumber = accountNumber,
        accountNickname = accountNickname,
        accountType = accountType.type,
        balance = balance,
        currencyCode = currencyCode,
        beneficiaries = this.details?.beneficiaries ?: Constants.EMPTY,
        branch = this.details?.branch ?: Constants.EMPTY,
        openedDate = this.details?.openedDate ?: Constants.EMPTY,
        productName = this.details?.productName ?: Constants.EMPTY
    )

    fun AccountEntity.toDomain() = Account(
        id = id,
        accountNumber = accountNumber,
        accountNickname = accountNickname,
        accountType = AccountType[accountType],
        balance = balance,
        currencyCode = currencyCode
    ).apply {
        if(beneficiaries.isNotBlank() &&
            branch.isNotBlank() &&
            openedDate.isNotBlank() &&
            productName.isNotBlank()
        ) {
            this.details = AccountDetails(
                beneficiaries, branch, openedDate, productName
            )
        }
    }

    fun List<AccountEntity>.entitiesToAccounts() = this.mapList { it.toDomain() }

    fun AccountRaw.toDomain() = Account(
        id = id,
        accountNumber = accountNumber,
        accountNickname = accountNickname,
        accountType = AccountType[accountType],
        balance = balance,
        currencyCode = currencyCode
    )

    fun List<AccountRaw>.dtoToAccounts() = this.mapList { it.toDomain() }

    fun AccountDetailsDto.toDomain() = AccountDetails(
        beneficiaries = this.beneficiaries.joinToString(),
        branch = this.branch,
        openedDate = this.openedDate,
        productName = this.productName
    )

    fun AccountTransactionsDto.toPagedAccountTransactions(currencySymbol: String? = null) = PagedTransactions(
        paging = this.paging.toDomain(),
        transactions = this.transactions
            .mapList { it.toDomain(currencySymbol ?: "") }
            .sortedByDescending {
            it.date
        }
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