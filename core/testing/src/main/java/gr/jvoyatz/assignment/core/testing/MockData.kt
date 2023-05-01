package gr.jvoyatz.assignment.core.testing

import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.AccountType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

object MockData {
    //API
    const val RESPONSE = "test response"
    const val MOCK_NET_ERROR_RESPONSE = "{\"error\": [\"errorError\"]}"
    val MOCK_RESPONSE_BODY = MOCK_NET_ERROR_RESPONSE.toResponseBody("application/json".toMediaTypeOrNull())



    val account = Account(
        "id1",
        "",
        1234,
        AccountType.CREDIT_CARD,
        "",
        "",
        false
    )

}