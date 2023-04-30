package gr.jvoyatz.assignment.wallet.common.android.ui.mappers

import gr.jvoyatz.assignment.core.common.utils.DateUtils
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountDetailsUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountTransactionUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.models.PagingUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.models.TransactionUI
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.AccountDetails
import gr.jvoyatz.assignment.wallet.domain.models.Paging
import gr.jvoyatz.assignment.wallet.domain.models.Transaction

fun Account.toUiModel() = AccountUiModel(
    id = id,
    accountNickname = accountNickname,
    accountNumber = accountNumber,
    accountType = accountType,
    balance = balance,
    currencyCode = currencyCode,
    isFavorite = isFavorite,
    details = details?.toUiModel(),
    transactions = pagedTransactions?.transactions?.toUiModels() ?: null,
    paging = pagedTransactions?.paging?.toUiModel() ?: null
)
fun AccountDetails.toUiModel() = AccountDetailsUiModel(
    beneficiaries = this.beneficiaries,
    branch = this.branch,
    openedDate = this.openedDate,
    productName = this.productName
)

fun Paging.toUiModel() = PagingUiModel(
    currentPage= this.currentPage,
    pagesCount = this.pagesCount,
    totalItems = this.totalItems
)

fun Transaction.toUiModel() = AccountTransactionUiModel(
    DateUtils.toDateFormat3(date),
    description,
    id,
    isDebit,
    transactionAmount,
    transactionType,
)

fun List<Transaction>.toUiModels(): List<TransactionUI> {
    val list: MutableList<TransactionUI> = mutableListOf()
    map { it.toUiModel() }.groupBy({it.date}, { it }).forEach {
        list.add(TransactionUI.Date(it.key))
        list.addAll(it.value.map { element -> TransactionUI.Holder(element) })
    }

    return map { tr -> tr.toUiModel() }
        .groupBy({ uiModel -> uiModel.date }, { uiModel ->  uiModel })
        .flatMap { entry ->
            listOf(TransactionUI.Date(entry.key)) + entry.value.map { TransactionUI.Holder(it) }
        }
}


//used when setting an account as selected
fun AccountUiModel.toDomain() = Account(
    id = id,
    accountNickname = accountNickname,
    accountNumber = accountNumber,
    accountType = accountType,
    balance = balance,
    currencyCode = currencyCode,
    isFavorite = isFavorite
)
