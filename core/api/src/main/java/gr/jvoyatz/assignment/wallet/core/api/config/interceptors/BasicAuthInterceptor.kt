package gr.jvoyatz.assignment.wallet.core.api.config.interceptors

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * We make our outgoing request to use a custom Interceptor.
 *
 * This interceptor includes the "Authorization" header using [Credentials.basic] method to generate this
 * header's value
 */

const val AUTHORIZATION = "Authorization"
internal class BasicAuthInterceptor(username: String, password: String): Interceptor {
    private val credentials: String = Credentials.basic(username, password)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return with(chain.request()) {
            this.newBuilder()
                .header(AUTHORIZATION, credentials)
        }.build().run {
            chain.proceed(this)
        }
    }
}