package gr.jvoyatz.afse.wallet.core.api


import com.google.common.truth.Truth
import gr.jvoyatz.afse.core.testing.MockData
import gr.jvoyatz.afse.wallet.core.api.config.ApiResponse.HttpError
import gr.jvoyatz.afse.wallet.core.api.config.ApiResponse.NetworkError
import gr.jvoyatz.afse.wallet.core.api.config.asError
import gr.jvoyatz.afse.wallet.core.api.config.asHttpError
import gr.jvoyatz.afse.wallet.core.api.config.asNetworkError
import gr.jvoyatz.afse.wallet.core.api.config.asSuccess
import gr.jvoyatz.afse.wallet.core.api.config.asUnexpectedError
import gr.jvoyatz.afse.wallet.core.api.config.isSuccess
import gr.jvoyatz.afse.wallet.core.api.config.isSuccessEmpty
import gr.jvoyatz.afse.wallet.core.api.config.safeApiCall
import gr.jvoyatz.afse.wallet.core.api.config.safeRawApiCall
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
class ApiResponseCallUtilsTest {

    @Test
    fun `when response returns successfully, then returns success`() = runTest {
        //given
        val body = true

        //when
        val result = safeApiCall<Boolean, Unit> { Response.success(body) }

        //then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result.isSuccess()).isTrue()
        Truth.assertThat(result.asSuccess()!!.body).isEqualTo(body)
    }

    @Test
    fun `safe api call with empty(null) body, returns api unknown error empty`() = runTest {
        //given
        val body = null

        //when
        val response = safeApiCall<String, Unit> {
            Response.success(body)
        }

        Truth.assertThat(response).isNotNull()
        Truth.assertThat(response.asError()).isInstanceOf(UnknownError::class.java)
    }

    @Test
    fun `safe api call with empty(null) body, returns api success empty`() = runTest {
        //given
        val body = null

        //when
        val response = safeApiCall<Unit, Unit> {
            Response.success(body)
        }

        Truth.assertThat(response).isNotNull()
        Truth.assertThat(response.isSuccessEmpty()).isTrue()
    }

    @Test
    fun `safe api call with unit body, returns api success empty`() = runTest {
        //given
        val body = Unit

        //when
        val response = safeApiCall<Unit, Unit> {
            Response.success(body)
        }

        Truth.assertThat(response).isNotNull()
        Truth.assertThat(response.isSuccessEmpty()).isTrue()
    }

    @Test
    fun `safe api call http error bad request, returns http error `() = runTest {
        //given
        val body = MockData.MOCK_RESPONSE_BODY

        //when
        val errorResponse = safeApiCall<Boolean, String>({
            MockData.MOCK_NET_ERROR_RESPONSE
        }) {
            Response.error(400, body)
        }

        //then
        Truth.assertThat(errorResponse).isInstanceOf(HttpError::class.java)
        Truth.assertThat(errorResponse.asHttpError()!!.code).isEqualTo(400)
        Truth.assertThat(errorResponse.asHttpError()!!.errorBody)
            .isEqualTo(MockData.MOCK_NET_ERROR_RESPONSE)
    }

    @Test
    fun `safe api call network error, returns api network error `() = runTest {
        //when
        val errorResponse = safeApiCall<Unit, Unit> {
            throw IOException("io exception ")
        }

        //then
        Truth.assertThat(errorResponse).isInstanceOf(NetworkError::class.java)
        Truth.assertThat(errorResponse.asNetworkError()!!.error)
            .isInstanceOf(IOException::class.java)
    }

    @Test
    fun `safe api call unexpected error, returns api unknown error `() = runTest {
        //when
        val errorResponse = safeApiCall<String, String> {
            throw IllegalStateException("test state exception")
        }

        //then
        Truth.assertThat(errorResponse).isInstanceOf(UnknownError::class.java)
        Truth.assertThat(errorResponse.asUnexpectedError()!!.error)
            .isInstanceOf(IllegalStateException::class.java)
    }


//    @Test
//    fun `safe api call http error bad request, returns http error 2 `() = runTest {
//        //given
//        val body = RetrofitMockData.ERROR_RESPONSE_BODY
//
//        //when
//        val errorResponse = safeApiCall<Boolean, MockError> {
//            Response.error(400, body)
//        }
//
//        //then
//        Truth.assertThat(errorResponse).isInstanceOf(HttpError::class.java)
//        Truth.assertThat(errorResponse.asHttpError()!!.code).isEqualTo(400)
//    //    Truth.assertThat(errorResponse.asHttpError()!!.errorBody).isEqualTo(RetrofitMockData.ERROR_RESPONSE)
//    }

    @Test
    fun ` safe raw api call, when response returns successfully, then returns success`() = runTest {
        //given
        val body = true

        //when
        val result = safeRawApiCall<Boolean, Unit> { body }

        //then
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result.isSuccess()).isTrue()
        Truth.assertThat(result.asSuccess()!!.body).isEqualTo(body)
    }

    @Test
    fun `safe raw api call with empty(null) body, returns api unknown error empty`() = runTest {
        //given
        val body = null

        //when
        val response = safeRawApiCall<String, Unit> {
            body!!
        }

        Truth.assertThat(response).isNotNull()
        Truth.assertThat(response.asError()).isInstanceOf(UnknownError::class.java)
    }

    @Test
    fun `safe raw api call with empty(null) body, returns api success empty`() = runTest {
        //given
        val body = null

        //when
        val response = safeApiCall<Unit, Unit> {
            Response.success(body)
        }

        Truth.assertThat(response).isNotNull()
        Truth.assertThat(response.isSuccessEmpty()).isTrue()
    }

    @Test
    fun `safe raw api call with unit body, returns api success empty`() = runTest {
        //given
        val body = Unit

        //when
        val response = safeApiCall<Unit, Unit> {
            Response.success(body)
        }

        Truth.assertThat(response).isNotNull()
        Truth.assertThat(response.isSuccessEmpty()).isTrue()
    }

    @Test
    fun `safe raw api call http error bad request, returns http error `() = runTest {
        //given
        val body = MockData.MOCK_RESPONSE_BODY

        //when
        val errorResponse = safeRawApiCall<Boolean, String>({
            MockData.MOCK_NET_ERROR_RESPONSE
        }) {
            throw HttpException(Response.error<String>(400, body))
        }

        //then
        Truth.assertThat(errorResponse).isInstanceOf(HttpError::class.java)
        Truth.assertThat(errorResponse.asHttpError()!!.code).isEqualTo(400)
        Truth.assertThat(errorResponse.asHttpError()!!.errorBody)
            .isEqualTo(MockData.MOCK_NET_ERROR_RESPONSE)
    }

    @Test
    fun `safe raw api call network error, returns api network error `() = runTest {
        //when
        val errorResponse = safeRawApiCall<Unit, Unit> {
            throw IOException("io exception ")
        }

        //then
        Truth.assertThat(errorResponse).isInstanceOf(NetworkError::class.java)
        Truth.assertThat(errorResponse.asNetworkError()!!.error)
            .isInstanceOf(IOException::class.java)
    }

    @Test
    fun `safe raw api call unexpected error, returns api unknown error `() = runTest {
        //when
        val errorResponse = safeRawApiCall<String, String> {
            throw IllegalStateException("test state exception")
        }

        //then
        Truth.assertThat(errorResponse).isInstanceOf(UnknownError::class.java)
        Truth.assertThat(errorResponse.asUnexpectedError()!!.error)
            .isInstanceOf(IllegalStateException::class.java)
    }
}