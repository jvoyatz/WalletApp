package gr.jvoyatz.assignment.wallet.core.api.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionsPagedRequest(
    @Json(name = "from_date")
    val fromDate: String?=null,
    @Json(name = "next_page")
    val nextPage: Int = 0,
    @Json(name = "to_date")
    val toDate: String?=null
)