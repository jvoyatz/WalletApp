package gr.jvoyatz.afse.core.common

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Sealed interface with two possible states
 *  1) Success
 *  2) Error
 *
 *  Acts as a wrapper for data.
 *
 *  Can be used as well to wrap the result of given a block
 *  into a instance of this class.
 */
sealed interface ResultData<out T>{
    data class Success<T>(val data: T): ResultData<T>

    data class Error(val exception: Throwable? = null): ResultData<Nothing>

    companion object{
        /**
         * Creates an instance of the Success instance
         */
        fun <T> success(data: T): Success<T> = Success(data)

        /**
         * Creates an instance of the Error instance
         */
        fun error(throwable: Throwable?): Error = Error(throwable)
    }
}


fun <T> ResultData<T>.isSuccess(): Boolean = this is ResultData.Success

fun <T> ResultData<T>.isError(): Boolean = this is ResultData.Error

fun <T> ResultData<T>.asSuccess(): ResultData.Success<T>? {
    return if (this is ResultData.Success){
        return this
    }else{
        null
    }
}

fun <T> ResultData<T>.asError(): ResultData.Error? {
    return if (this is ResultData.Error){
        return this
    }else{
        null
    }
}

inline fun <T> ResultData<T>.onSuccess(crossinline action: (value: T) -> Unit): ResultData<T> {
    if(isSuccess()) action(asSuccess()!!.data)
    return this
}

inline fun <T> ResultData<T>.onError(crossinline action: (value: Throwable) -> Unit): ResultData<T> {
    if(isError()) asError()!!.exception?.let{
        action(it)
    }
    return this
}

/**
 * Better management of exceptions when using coroutines.
 *
 * Based on these articles:
 * https://proandroiddev.com/resilient-use-cases-with-kotlin-result-coroutines-and-annotations-511df10e2e16
 * See https://github.com/Kotlin/kotlinx.coroutines/issues/1814.
 */
inline fun <R> resultOf(crossinline block: () -> R): ResultData<R> {
    return try {
        ResultData.success(block())
    } catch (t: TimeoutCancellationException) {
        ResultData.error(t)
    } catch (c: CancellationException) {
        throw c
    } catch (e: Exception) {
        ResultData.error(e)
    }
}

/**
 * Converts the values emitted in this flow
 * to be wrapped inside
 */
fun <T> Flow<T>.asResult(): Flow<ResultData<T>> =
    this.map {
        resultOf { it }
    }.catch {
        if(it !is CancellationException) {
            emit(ResultData.error(it))
        }else {
            throw it
        }
    }
