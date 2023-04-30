package gr.jvoyatz.assignment.wallet.domain.models


data class PagedTransactions(
    val paging: Paging,
    val transactions: List<Transaction>
)

data class Paging(
    val currentPage: Int = UNKNOWN,
    val pagesCount: Int= UNKNOWN,
    val totalItems: Int = UNKNOWN
){
    companion object {
        const val UNKNOWN = -1
        const val PAGE_SIZE = 10
    }
}

/**
 * Contains info for a specific transaction happened in the past
 */
data class Transaction(
    val date: String,
    val description: String?,
    val id: String,
    val isDebit: Boolean,
    val transactionAmount: String,
    val transactionType: String
)