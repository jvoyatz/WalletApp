package gr.jvoyatz.assignment.wallet.core.api.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountDetailsDto(
    @Json(name = "beneficiaries")
    val beneficiaries: List<String>,
    @Json(name = "branch")
    val branch: String,
    @Json(name = "opened_date")
    val openedDate: String,
    @Json(name = "product_name")
    val productName: String
)