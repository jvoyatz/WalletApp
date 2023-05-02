@file:Suppress("unused")

package gr.jvoyatz.assignment.core.common.resultdata


/**
 * Returns true if this instance is a type of [ResultData.Success]
 */
fun <T> ResultData<T>.isSuccess(): Boolean = this is ResultData.Success

/**
 * Returns true if this instance is a type of [ResultData.Error]
 */
fun <T> ResultData<T>.isError(): Boolean = this is ResultData.Error

/**
 * If this instance is a type of [ResultData.Success] then it casts it
 * to this type, otherwise it returns null.
 */
fun <T> ResultData<T>.asSuccess(): ResultData.Success<T>? {
    return if (this is ResultData.Success){
        return this
    }else{
        null
    }
}
/**
 * If this instance is a type of [ResultData.Error] then it casts it
 * to this type, otherwise it returns null.
 */
fun <T> ResultData<T>.asError(): ResultData.Error? {
    return if (this is ResultData.Error){
        return this
    }else{
        null
    }
}

/**
 * Executes the given block in case of a [ResultData.Success] instance, otherwise it does nothing
 */
inline fun <T> ResultData<T>.onSuccess(/*crossinline*/ action: (value: T) -> Unit): ResultData<T> {
    if(isSuccess()) action(asSuccess()!!.data)
    return this
}

suspend inline fun <T> ResultData<T>.onSuspendedSuccess(crossinline action: suspend (value: T) -> Unit): ResultData<T> {
    if(isSuccess()) action(asSuccess()!!.data)
    return this
}
/**
 * Executes the given block in case of a [ResultData.Error] instance, otherwise it does nothing
 */
inline fun <T> ResultData<T>.onError(crossinline action: (value: Throwable) -> Unit): ResultData<T> {
    if(isError()) asError()!!.exception?.let{
        action(it)
    }
    return this
}

suspend inline fun <T> ResultData<T>.onSuspendedError(crossinline action: suspend (value: Throwable) -> Unit): ResultData<T> {
    if(isError()) asError()!!.exception?.let{
        action(it)
    }
    return this
}

/**
 * Executes the given block in any case of a [ResultData] instance
 */
inline fun <T> ResultData<T>.onAny(crossinline action: () -> Unit): ResultData<T> {
    action()
    return this
}

inline fun <T, R> ResultData<T>.mapSuccess(mapper: T.() -> R): ResultData<R> {
    return when (this) {
        is ResultData.Success -> data.mapper().toResultDataSuccess()
        is ResultData.Error -> this
    }
}