package gr.jvoyatz.afse.core.testing

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

object MockData {
    //API
    const val RESPONSE = "test response"
    const val MOCK_NET_ERROR_RESPONSE = "{\"error\": [\"errorError\"]}"
    val MOCK_RESPONSE_BODY = MOCK_NET_ERROR_RESPONSE.toResponseBody("application/json".toMediaTypeOrNull())


}