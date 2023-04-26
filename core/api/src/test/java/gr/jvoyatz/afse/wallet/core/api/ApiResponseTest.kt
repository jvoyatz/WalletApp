package gr.jvoyatz.afse.wallet.core.api

import com.google.common.truth.Truth
import gr.jvoyatz.afse.wallet.core.api.config.response.ApiResponse
import gr.jvoyatz.afse.wallet.core.api.config.response.ApiResponse.ApiSuccess
import gr.jvoyatz.afse.wallet.core.api.config.response.ApiResponse.HttpError
import gr.jvoyatz.afse.wallet.core.api.config.response.ApiResponse.NetworkError
import gr.jvoyatz.afse.wallet.core.api.config.response.ApiResponse.UnexpectedError
import gr.jvoyatz.afse.wallet.core.api.config.response.asError
import gr.jvoyatz.afse.wallet.core.api.config.response.asHttpError
import gr.jvoyatz.afse.wallet.core.api.config.response.asNetworkError
import gr.jvoyatz.afse.wallet.core.api.config.response.asSuccess
import gr.jvoyatz.afse.wallet.core.api.config.response.asUnexpectedError
import gr.jvoyatz.afse.wallet.core.api.config.response.getOrNull
import gr.jvoyatz.afse.wallet.core.api.config.response.isError
import gr.jvoyatz.afse.wallet.core.api.config.response.isSuccess
import gr.jvoyatz.afse.wallet.core.api.config.response.isSuccessEmpty
import gr.jvoyatz.afse.wallet.core.api.config.response.onError
import gr.jvoyatz.afse.wallet.core.api.config.response.onHttpError
import gr.jvoyatz.afse.wallet.core.api.config.response.onNetworkError
import gr.jvoyatz.afse.wallet.core.api.config.response.onSuccess
import gr.jvoyatz.afse.wallet.core.api.config.response.onSuspendedError
import gr.jvoyatz.afse.wallet.core.api.config.response.onSuspendedHttpError
import gr.jvoyatz.afse.wallet.core.api.config.response.onSuspendedNetworkError
import gr.jvoyatz.afse.wallet.core.api.config.response.onSuspendedSuccess
import gr.jvoyatz.afse.wallet.core.api.config.response.onSuspendedUnknownError
import gr.jvoyatz.afse.wallet.core.api.config.response.onUnknownError
import gr.jvoyatz.afse.wallet.core.api.config.response.toFlow
import gr.jvoyatz.afse.wallet.core.api.config.response.toSuspendFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ApiResponseTest {

    val success = ApiResponse.success<String, Unit>("234")
    val successEmpty = ApiResponse.success<Unit, Unit>(Unit)
    val httpError = ApiResponse.httpError<String, String>(234, "http error")
    val unknownError =
        ApiResponse.unexpectedError<String, String>(NullPointerException("unknownerror"))
    val networkError =
        ApiResponse.networkError<String, String>(IllegalStateException("networkerror"))

    @Test
    fun `test that httpError() returns HttpError instance`() {
        //given
        val code = 1
        val bodyStr = "error"

        //when
        val httpError = ApiResponse.httpError<Unit, String>(code, bodyStr)

        //then
        Truth.assertThat(httpError).isInstanceOf(HttpError::class.java)
    }

    @Test
    fun `test that unknownError() returns unknownError instance`() {
        //given
        val throwable = IllegalStateException()

        //when
        val unknownError = ApiResponse.unexpectedError<Any, Any>(throwable)

        //then
        Truth.assertThat(unknownError).isInstanceOf(UnexpectedError::class.java)
    }

    @Test
    fun `test that networkError() returns networkError instance`() {
        //given
        val throwable = RuntimeException()

        //when
        val networkError = ApiResponse.networkError<Any, Any>(throwable)

        //then
        Truth.assertThat(networkError).isInstanceOf(NetworkError::class.java)
    }

    @Test
    fun `test that success() returns success instance`() {
        //given
        val data = "1"

        //when
        val success = ApiResponse.success<String, Unit>(data)

        //then
        Truth.assertThat(success).isInstanceOf(ApiSuccess::class.java)
    }

    @Test
    fun `asSuccess returns nonNull when success instance`() {
        //given
        val data = "1"

        //when
        val success = ApiResponse.success<String, Unit>(data)

        //then
        Truth.assertThat(success.isSuccess()).isTrue()
        Truth.assertThat(success.asSuccess()).isNotNull()
    }

    @Test
    fun `asSuccess returns null when non success instance`() {
        //when
        val success = ApiResponse.networkError<String, Unit>(null)

        //then
        Truth.assertThat(success.asSuccess()).isNull()
    }

    @Test
    fun `asError returns nonNull when error instance`() {
        //when
        val networkError = ApiResponse.networkError<String, Unit>(null)

        //then
        Truth.assertThat(networkError.asError()).isNotNull()
    }

    @Test
    fun `asError returns null when success instance`() {
        //when
        val success = ApiResponse.success<String, Unit>("234")

        //then
        Truth.assertThat(success.asError()).isNull()
    }

    @Test
    fun `asHttpError returns nonNull when HttpError instance`() {
        //when
        val httpError = ApiResponse.httpError<String, Unit>(1, Unit)

        //then
        Truth.assertThat(httpError.asHttpError()).isNotNull()
    }

    @Test
    fun `asHttpError returns null when other instance`() {
        //when
        val success = ApiResponse.success<String, Unit>("234")

        //then
        Truth.assertThat(success.asHttpError()).isNull()
    }

    @Test
    fun `asNetworkError returns nonNull when NetworkError instance`() {
        //when
        val networkError = ApiResponse.networkError<String, Unit>(null)

        //then
        Truth.assertThat(networkError.asNetworkError()).isNotNull()
    }

    @Test
    fun `asNetworkError returns null in case of another instance`() {
        //when
        val success = ApiResponse.success<String, Unit>("234")

        //then
        Truth.assertThat(success.asNetworkError()).isNull()
    }

    @Test
    fun `asUnknownError returns nonNull when UnknownError instance`() {
        //when
        val unknownError = ApiResponse.unexpectedError<String, Unit>(null)

        //then
        Truth.assertThat(unknownError.asUnexpectedError()).isNotNull()
    }

    @Test
    fun `asUnknownError returns null when non UnknownError instance`() {
        //when
        val success = ApiResponse.success<String, Unit>("234")

        //then
        Truth.assertThat(success.asUnexpectedError()).isNull()
    }

    @Test
    fun `isSuccess returns true when ApiSuccess instance`() {
        //when
        val isSuccess = success.isSuccess()

        //then
        Truth.assertThat(isSuccess).isTrue()
    }

    @Test
    fun `isSuccess returns false when non ApiSuccess instance`() {
        //when
        val isSuccess = httpError.isSuccess()

        //then
        Truth.assertThat(isSuccess).isFalse()
    }

    @Test
    fun `isSuccessEmpty returns true when ApiSuccess and empty body instance`() {
        //when
        val isEmptySuccess = successEmpty.isSuccessEmpty()

        //then
        Truth.assertThat(isEmptySuccess).isTrue()
    }

    @Test
    fun `isSuccessEmpty returns false when  ApiSuccess and other type body instance`() {
        //when
        val isEmptySuccess = success.isSuccessEmpty()

        //then
        Truth.assertThat(isEmptySuccess).isFalse()
    }

    @Test
    fun `isSuccessEmpty returns false when non  ApiSuccess`() {
        //when
        val isEmptySuccess = httpError.isSuccessEmpty()

        //then
        Truth.assertThat(isEmptySuccess).isFalse()
    }

    @Test
    fun `isError returns true when HttpError instance`() {
        //when
        val isError = httpError.isError()

        //then
        Truth.assertThat(isError).isTrue()
    }

    @Test
    fun `isError returns true when any ApiError instance`() {
        //when
        val isError = unknownError.isError()

        //then
        Truth.assertThat(isError).isTrue()
    }

    @Test
    fun `isError returns false when ApiSuccess instance`() {
        //when
        val isError = success.isError()

        //then
        Truth.assertThat(isError).isFalse()
    }

    @Test
    fun `getOrNull returns not null data when ApiSuccess`() {
        //given
        val body = "234"

        //when
        val data = success.getOrNull()

        //then
        Truth.assertThat(data).isNotNull()
        Truth.assertThat(data).isEqualTo(body)
    }

    @Test
    fun `invoke() returns null data when any other instance`() {
        //when
        val data = httpError.getOrNull()

        //then
        Truth.assertThat(data).isNull()
    }

    @Test
    fun `invoke() returns not null data when ApiSuccess`() {
        //given
        val body = "234"

        //when
        val data = success.getOrNull()

        //then
        Truth.assertThat(data).isNotNull()
        Truth.assertThat(data).isEqualTo(body)
    }

    @Test
    fun `getOrNull returns null data when any other instance`() {
        //when
        val data = httpError.getOrNull()

        //then
        Truth.assertThat(data).isNull()
    }

    @Test
    fun `onSuccess is executed when apiResponse is of an ApiSuccess instance`() {
        //given
        var isExecuted = false

        //when
        success.onSuccess {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isTrue()
    }

    @Test
    fun `onSuccess is not executed when apiResponse is not an ApiSuccess instance`() {
        //given
        var isExecuted = false

        //when
        httpError.onSuccess {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isFalse()
    }

    @Test
    fun `onSuccess with map block is executed when apiResponse is of an ApiSuccess instance`() {
        //given
        var isExecuted = false

        //when
        success.onSuccess({
            isExecuted = true
            1
        }) {}

        //then
        Truth.assertThat(isExecuted).isTrue()
    }

    @Test
    fun `onSuccess with map is not executed when apiResponse is not an ApiSuccess instance`() {
        //given
        var isExecuted = false

        //when
        httpError.onSuccess({
            isExecuted = true
            1
        }) {}

        //then
        Truth.assertThat(isExecuted).isFalse()
    }

    @Test
    fun `onSuspendedSuccess is executed when apiResponse is of an ApiSuccess instance`() = runTest {
        //given
        var isExecuted = false

        //when
        success.onSuspendedSuccess {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isTrue()
    }

    @Test
    fun `onSuspendedSuccess is not executed when apiResponse is not an ApiSuccess instance`() =
        runTest {
            //given
            var isExecuted = false

            //when
            httpError.onSuspendedSuccess {
                isExecuted = true
            }

            //then
            Truth.assertThat(isExecuted).isFalse()
        }

    @Test
    fun `onSuspendedSuccess with map block is executed when apiResponse is of an ApiSuccess instance`() =
        runTest {
            //given
            var isExecuted = false

            //when
            success.onSuspendedSuccess({
                isExecuted = true
                1
            }) {}

            //then
            Truth.assertThat(isExecuted).isTrue()
        }

    @Test
    fun `onSuspendedSuccess with map is not executed when apiResponse is not an ApiSuccess instance`() =
        runTest {
            //given
            var isExecuted = false

            //when
            httpError.onSuspendedSuccess({
                isExecuted = true
                1
            }) {}

            //then
            Truth.assertThat(isExecuted).isFalse()
        }


    @Test
    fun `onError is executed when apiResponse is an ApiError instance`() {
        //given
        var isExecuted = false

        //when
        httpError.onError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isTrue()
    }

    @Test
    fun `onError is not executed when apiResponse is of an ApiSuccess instance`() {
        //given
        var isExecuted = false

        //when
        success.onError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isFalse()
    }

    @Test
    fun `onSuspendedError is executed when apiResponse is an ApiError instance`() = runTest {
        //given
        var isExecuted = false

        //when
        httpError.onSuspendedError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isTrue()
    }

    @Test
    fun `onSuspendedError is not executed when apiResponse is of an ApiSuccess instance`() =
        runTest {
            //given
            var isExecuted = false

            //when
            success.onSuspendedError {
                isExecuted = true
            }

            //then
            Truth.assertThat(isExecuted).isFalse()
        }

    @Test
    fun `onHttpError is executed when apiResponse is an HttpError instance`() {
        //given
        var isExecuted = false

        //when
        httpError.onHttpError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isTrue()
    }

    @Test
    fun `onHttpError is not executed when apiResponse is not of an HttpError instance`() {
        //given
        var isExecuted = false

        //when
        success.onHttpError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isFalse()
    }

    @Test
    fun `onSuspendedHttpError is executed when apiResponse is an HttpError instance`() = runTest {
        //given
        var isExecuted = false

        //when
        httpError.onSuspendedHttpError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isTrue()
    }

    @Test
    fun `onSuspendedHttpError is not executed when apiResponse is not of an HttpError instance`() =
        runTest {
            //given
            var isExecuted = false

            //when
            success.onSuspendedHttpError {
                isExecuted = true
            }

            //then
            Truth.assertThat(isExecuted).isFalse()
        }

    @Test
    fun `onNetworkError is executed when apiResponse is an NetworkError instance`() {
        //given
        var isExecuted = false

        //when
        networkError.onNetworkError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isTrue()
    }

    @Test
    fun `onNetworkError is not executed when apiResponse is not of an NetworkError instance`() {
        //given
        var isExecuted = false

        //when
        success.onNetworkError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isFalse()
    }

    @Test
    fun `onSuspendedNetworkError is executed when apiResponse is an NetworkError instance`() =
        runTest {
            //given
            var isExecuted = false

            //when
            networkError.onSuspendedNetworkError {
                isExecuted = true
            }

            //then
            Truth.assertThat(isExecuted).isTrue()
        }

    @Test
    fun `onSuspendedNetworkError is not executed when apiResponse is not of an NetworkError instance`() =
        runTest {
            //given
            var isExecuted = false

            //when
            success.onSuspendedNetworkError {
                isExecuted = true
            }

            //then
            Truth.assertThat(isExecuted).isFalse()
        }


    @Test
    fun `onUnknownError is executed when apiResponse is an UnknownError instance`() {
        //given
        var isExecuted = false

        //when
        unknownError.onUnknownError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isTrue()
    }

    @Test
    fun `onUnknownError is not executed when apiResponse is not of an UnknownError instance`() {
        //given
        var isExecuted = false

        //when
        success.onNetworkError {
            isExecuted = true
        }

        //then
        Truth.assertThat(isExecuted).isFalse()
    }

    @Test
    fun `onSuspendedNetworkError is executed when apiResponse is an UnknownError instance`() =
        runTest {
            //given
            var isExecuted = false

            //when
            unknownError.onSuspendedUnknownError {
                isExecuted = true
            }

            //then
            Truth.assertThat(isExecuted).isTrue()
        }

    @Test
    fun `onSuspendedNetworkError is not executed when apiResponse is not of an UnknownError instance`() =
        runTest {
            //given
            var isExecuted = false

            //when
            success.onSuspendedUnknownError {
                isExecuted = true
            }

            //then
            Truth.assertThat(isExecuted).isFalse()
        }

    @Test
    fun `apiResponse toFlow emit the body type when success`() = runTest {
        //when
        val flow = success.toFlow()
        val data = flow.first()

        //then
        Truth.assertThat(data).isEqualTo("234")
    }

    @Test
    fun `apiResponse toFlow emit nothing when error`() = runTest {
        //when
        val flow = httpError.toFlow()
        val data = flow.toList()

        //then
        Truth.assertThat(data).isEmpty()
    }

    @Test
    fun `apiResponse toFlow with map block func emit the body type when success`() = runTest {
        //when
        val flow = success.toFlow {
            1
        }
        val data = flow.first()

        //then
        Truth.assertThat(data).isEqualTo(1)
    }

    @Test
    fun `apiResponse toFlow with map block func emit nothing when error`() = runTest {
        //when
        val flow = httpError.toFlow {
            1
        }
        val data = flow.toList()

        //then
        Truth.assertThat(data).isEmpty()
    }

    @Test
    fun `apiResponse toSuspendFlow with map block func emit nothing when error`() = runTest {
        //when
        val flow = httpError.toSuspendFlow {
            1
        }
        val data = flow.toList()

        //then
        Truth.assertThat(data).isEmpty()
    }

    @Test
    fun `apiResponse toSuspendFlow with map block func emit data when success`() = runTest {
        //when
        val flow = httpError.toSuspendFlow {
            1
        }
        val data = flow.toList()

        //then
        Truth.assertThat(data).isEmpty()
    }
}