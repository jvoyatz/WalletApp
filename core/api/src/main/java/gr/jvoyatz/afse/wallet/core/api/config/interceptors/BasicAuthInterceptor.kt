package gr.jvoyatz.afse.wallet.core.api.config.interceptors

import gr.jvoyatz.afse.wallet.core.api.config.AUTHORIZATION
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

/**
 * We make our outgoing request to use a custom Interceptor.
 *
 * This interceptor includes the "Authorization" header using [Credentials.basic] method to generate this
 * header's value
 */
internal class BasicAuthInterceptor(username: String, password: String): Interceptor {
    private val credentials: String = Credentials.basic(username, password)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
         return with(chain.request()) {
            this.newBuilder()
                .header(AUTHORIZATION, credentials)
        }.let {
            it.build()
        }.run {
            chain.proceed(this)
         }
    }
}