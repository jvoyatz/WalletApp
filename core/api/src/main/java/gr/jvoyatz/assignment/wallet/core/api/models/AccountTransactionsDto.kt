package gr.jvoyatz.assignment.wallet.core.api.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountTransactionsDto(
    @Json(name = "paging")
    val paging: PagingDto,
    @Json(name = "transactions")
    val transactions: List<TransactionRaw>
)
@JsonClass(generateAdapter = true)
data class PagingDto(
    @Json(name = "current_page")
    val currentPage: Int,
    @Json(name = "pages_count")
    val pagesCount: Int,
    @Json(name = "total_items")
    val totalItems: Int
)
@JsonClass(generateAdapter = true)
class TransactionRaw(
    @Json(name = "date")
    val date: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "is_debit")
    val isDebit: Boolean,
    @Json(name = "transaction_amount")
    val transactionAmount: String,
    @Json(name = "transaction_type")
    val transactionType: String
)