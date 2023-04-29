package gr.jvoyatz.assignment.core.common.resultdata

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


/**
 * Wrap any non nullable type to a [ResultData.Success] instance
 */
fun <T> T.toResultDataSuccess():ResultData<T> = ResultData.success(this)

/**
 * Better management of exceptions when using coroutines.
 *
 * Based on these articles:
 * https://proandroiddev.com/resilient-use-cases-with-kotlin-result-coroutines-and-annotations-511df10e2e16
 * See https://github.com/Kotlin/kotlinx.coroutines/issues/1814.
 *
 * This class wraps the given block inside [ResultData.Success] in case it
 * completes its execution without an exception.
 *
 * Otherwise, it distinguishes the exception type and in case of an [TimeoutCancellationException] or another exception [Exception]
 * it wraps this error inside an instance [ResultData.Error].
 *
 * What do we need to handle here? CancellationException (of Coroutines) need to be thrown
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

suspend inline fun <R> suspendedResultOf(crossinline block: suspend () -> R): ResultData<R> {
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
 * to be wrapped inside [ResultData]
 */
fun <T> Flow<T>.asResult(): Flow<ResultData<T>> =
    this.map {
        resultOf { it }
    }.catch {
        if (it !is CancellationException) {
            emit(ResultData.error(it))
        } else {
            throw it
        }
    }


