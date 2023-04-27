package gr.jvoyatz.assignment.wallet.core.api.config.response

import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse.ApiError
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse.ApiSuccess
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse.HttpError
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse.NetworkError
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse.UnexpectedError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf


fun <S, E> ApiResponse<S, E>.asSuccess(): ApiSuccess<S, E>? {
    if (this is ApiSuccess) {
        return this
    }
    return null
}

fun <S, E> ApiResponse<S, E>.asError(): ApiError<S, E>? {
    if (this is ApiError) {
        return this
    }
    return null
}

fun <S, E> ApiResponse<S, E>.asHttpError(): HttpError<S, E>? {
    if (this is HttpError) {
        return this
    }
    return null
}

fun <S, E> ApiResponse<S, E>.asNetworkError(): NetworkError<S, E>? {
    if (this is NetworkError) {
        return this
    }
    return null
}

fun <S, E> ApiResponse<S, E>.asUnexpectedError(): UnexpectedError<S, E>? {
    if (this is UnexpectedError) {
        return this
    }
    return null
}

fun <S, E> ApiResponse<S, E>.isSuccess() = this is ApiSuccess
fun <S, E> ApiResponse<S, E>.isSuccessEmpty() = (this is ApiSuccess) && this.body === Unit
fun <S, E> ApiResponse<S, E>.isError() = this is ApiError

fun <S, E> ApiResponse<S, E>.getOrNull(): S? {
    return when (this) {
        is ApiSuccess -> this.body
        else -> null
    }
}

/**
 * applies the given function, if network response
 * state is Success
 */
inline fun <S, E> ApiResponse<S, E>.onSuccess(
    crossinline onExecute: S.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is ApiSuccess) {
        onExecute(this.body)
    }
}

/**
 * applies the given function in case of successful response,
 * however it is transforming the body into another type
 */
inline fun <S, E, V> ApiResponse<S, E>.onSuccess(
    mapper: (S) -> V,
    crossinline onExecute: V.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is ApiSuccess) {
        onExecute(mapper(this.body))
    }
}

suspend inline fun <S, E> ApiResponse<S, E>.onSuspendedSuccess(
    crossinline onExecute: suspend S.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is ApiSuccess) {
        onExecute(this.body)
    }
}

suspend inline fun <S, E, V> ApiResponse<S, E>.onSuspendedSuccess(
    mapper: (S) -> V,
    crossinline onExecute: suspend V.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is ApiSuccess) {
        onExecute(mapper(this.body))
    }
}


/**
 * applies the given function, if network response
 * state is Success
 */
inline fun <S, E> ApiResponse<S, E>.onError(
    crossinline onExecute: ApiError<S, E>.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is ApiError) {
        onExecute(this)
    }
}

suspend inline fun <S, E> ApiResponse<S, E>.onSuspendedError(
    crossinline onExecute: suspend ApiError<S, E>.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is ApiError) {
        onExecute(this)
    }
}

/**
 * applies the given function, if network response
 * state is Success
 */
inline fun <S, E> ApiResponse<S, E>.onHttpError(
    crossinline onExecute: HttpError<S, E>.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is HttpError) {
        onExecute(this)
    }
}

suspend inline fun <S, E> ApiResponse<S, E>.onSuspendedHttpError(
    crossinline onExecute: suspend HttpError<S, E>.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is HttpError) {
        onExecute(this)
    }
}

/**
 * applies the given function, if network response
 * state is Success
 */
inline fun <S, E> ApiResponse<S, E>.onNetworkError(
    crossinline onExecute: NetworkError<S, E>.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is NetworkError) {
        onExecute(this)
    }
}

suspend inline fun <S, E> ApiResponse<S, E>.onSuspendedNetworkError(
    crossinline onExecute: suspend NetworkError<S, E>.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is NetworkError) {
        onExecute(this)
    }
}

/**
 * applies the given function, if network response
 * state is Success
 */
inline fun <S, E> ApiResponse<S, E>.onUnknownError(
    crossinline onExecute: UnexpectedError<S, E>.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is UnexpectedError) {
        onExecute(this)
    }
}

suspend inline fun <S, E> ApiResponse<S, E>.onSuspendedUnknownError(
    crossinline onExecute: suspend UnexpectedError<S, E>.() -> Unit
): ApiResponse<S, E> = apply {
    if (this is UnexpectedError) {
        onExecute(this)
    }
}

fun <S, E> ApiResponse<S, E>.toFlow(): Flow<S> {
    return if (this is ApiSuccess) {
        flowOf(this.body)
    } else {
        emptyFlow()
    }
}

inline fun <S, E, R> ApiResponse<S, E>.toFlow(
    crossinline mapper: S.() -> R
): Flow<R> {
    return if (this is ApiSuccess) {
        flowOf(this.body.mapper())
    } else {
        emptyFlow()
    }
}

suspend inline fun <S, E, R> ApiResponse<S, E>.toSuspendFlow(
    crossinline mapper: suspend S.() -> R
): Flow<R> {
    return if (this is ApiSuccess) {
        flowOf(this.body.mapper())
    } else {
        emptyFlow()
    }
}

/**
 * Overloading invoke operator to get the successful body otherwise null
 *
 * @param S the success body type
 * @param E the error body type
 *
 * Usage:
 *  val response = call.getSomething()
 *  response() ?: "null response"
 */
operator fun <S, E> ApiResponse<S, E>.invoke(): S? =
    if (this is ApiSuccess) body else null

