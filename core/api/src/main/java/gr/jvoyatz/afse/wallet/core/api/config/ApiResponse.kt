package gr.jvoyatz.afse.wallet.core.api.config


/**
 * It represents the state of the Retrofit response.
 *
 * The possible states are:
 *  success or error
 *
 *  S represents the body of a successful response
 *  E represents the body of the error response
 *
 *  When the response code is in the range 200-299 is considered as successful,
 *  otherwise error.
 *
 *  In case of a successful request as well as a successful deserialization of the response body
 *  then the response is ApiResponse.Success. When a response does not contain a
 *  body, then the S type should be declared as Unit.
 *
 *  When an error has been returned:
 *  1) HttpError with deserialized the error body
 *  2) IOExceptions are being represented as NetworkError
 *  3) For the rest errors/exceptions, UnknownError is being returned
 */
sealed interface ApiResponse<S, E> {

    /**
     * Successful response with data
     * In case where data are not available, then declare the S as Unit or VoidResponse
     */
    data class ApiSuccess<S, E>(val body: S) : ApiResponse<S, E>

    /**
     * sealed interface for Error
     */
    sealed interface ApiError<S, E> : ApiResponse<S, E>

    /**
     * Received a response with an error
     */
    data class HttpError<S, E>(val code: Int?, val errorBody: E?) : ApiError<S, E>

    /**
     * Network error, IO Exception
     */
    data class NetworkError<S, E>(val error: Throwable?) : ApiError<S, E>

    /**
     * Unknown Error during request
     */
    data class UnexpectedError<S, E>(val error: Throwable?) : ApiError<S, E>

    companion object {
        fun <S, E> httpError(code: Int? = null, errorBody: E? = null): ApiResponse<S, E> =
            HttpError(code, errorBody)

        fun <S, E> unexpectedError(throwable: Throwable? = null): ApiResponse<S, E> =
            UnexpectedError(throwable)

        fun <S, E> networkError(throwable: Throwable? = null): ApiResponse<S, E> =
            NetworkError(throwable)

        fun <S, E> success(body: S): ApiResponse<S, E> = ApiSuccess(body)
    }
}