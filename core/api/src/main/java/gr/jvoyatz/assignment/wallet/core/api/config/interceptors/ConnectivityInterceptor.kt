package gr.jvoyatz.assignment.wallet.core.api.config.interceptors

import android.content.Context
import gr.jvoyatz.assignment.core.common.exceptions.NoConnectionException
import gr.jvoyatz.assignment.wallet.common.android.utils.isOnline
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class ConnectivityInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!context.isOnline) {
            throw NoConnectionException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}
