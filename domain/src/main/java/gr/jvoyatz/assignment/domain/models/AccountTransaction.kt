package gr.jvoyatz.assignment.domain.models


data class AccountTransactions(
    val paging: Paging,
    val transactions: List<AccountTransaction>
)
data class Paging(
    val currentPage: Int,
    val pagesCount: Int,
    val totalItems: Int
)

/**
 * Contains info for a specific transaction happened in the past
 */
class AccountTransaction(
    val date: String,
    val description: String,
    val id: String,
    val isDebit: Boolean,
    val transactionAmount: String,
    val transactionType: String
)