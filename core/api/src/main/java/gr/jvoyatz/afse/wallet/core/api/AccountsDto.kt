package gr.jvoyatz.afse.wallet.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class AccountsDto(
    val accounts: List<AccountRaw>
){
    @JsonClass(generateAdapter = true)
    inner class AccountRaw(
        @Json(name = "account_nickname")
        val accountNickname: String?,
        @Json(name = "account_number")
        val accountNumber: Int,
        @Json(name = "account_type")
        val accountType: String,
        @Json(name = "balance")
        val balance: String,
        @Json(name = "currency_code")
        val currencyCode: String,
        @Json(name = "id")
        val id: String
    )
}