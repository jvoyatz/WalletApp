package gr.jvoyatz.assignment.wallet.core.api.config.response

import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

fun interface ErrorMapper<E>: (ResponseBody?) -> E?

/**
 * Receives a lambda functions, which returns
 * a retrofit response.
 * After this functions has been execute, the response is parsed
 * and a response containing the state of the HttpResponse is returned
 * see ApiResponse
 *
 * A downside using this functions is that you need to include it in every repository.
 * It could be reused (and improved) by using a retrofit call adapter
 */
suspend inline fun <reified S, reified E> safeRawApiCall(
    errorMapper: ErrorMapper<E> = ErrorMapper<E> { responseBody -> convertBody(E::class.java, responseBody) },
    crossinline apiCall: suspend () -> S): ApiResponse<S, E> {
    return try {
        val response = apiCall()
        if(response != null){
            ApiResponse.success(response)
        }else if(S::class.java == Unit::class.java) {
            @Suppress("UNCHECKED_CAST")
            ApiResponse.success<Unit, E>(Unit) as ApiResponse<S, E>
        }else{
            ApiResponse.unexpectedError(null)
        }
    } catch (t: Throwable) {
        when (t) {
            is JsonEncodingException -> ApiResponse.unexpectedError(t)
            is IOException -> ApiResponse.networkError(t)
            is HttpException -> ApiResponse.httpError(
                t.code(),
                errorMapper(t.response()?.errorBody())
            )
            else -> ApiResponse.unexpectedError(t)
        }
    }
}

/**
 * Same as above but handles Retrofit with Response<S> type
 */
suspend inline fun <reified S, reified E> safeApiCall(
    errorMapper: ErrorMapper<E> = ErrorMapper<E> { responseBody -> convertBody(E::class.java, responseBody) },
    crossinline execute: suspend () -> Response<S>): ApiResponse<S, E> {
    return try {
        val response = execute()

        if(response.isSuccessful) {
            val body = response.body()
            if(body != null) {
                ApiResponse.success(body)
            }else if(S::class.java === Unit::class.java){
                @Suppress("UNCHECKED_CAST")
                ApiResponse.success<Unit, E>(Unit) as ApiResponse<S, E>
            }else{
                ApiResponse.unexpectedError(null)
            }
        }else {
            ApiResponse.httpError(code = response.code(), errorMapper(response.errorBody()))
        }
    } catch (e: IOException) {
        ApiResponse.networkError(e)
    } catch (e: Exception){
        ApiResponse.unexpectedError(e)
    }
}

/**
 * Function used to convert the errorBody of a Response into a type
 */
@PublishedApi internal fun <E> convertBody(clazz: Class<E>, errorBody: ResponseBody?): E?{
    return try{
        errorBody?.source()?.let { src ->
            Moshi.Builder().build().adapter(clazz).let {
                it.fromJson(src)
            }
        }
    }catch (e: Exception){
        null
    }
}