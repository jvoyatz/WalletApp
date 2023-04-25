package gr.jvoyatz.afse.wallet.core.api


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import gr.jvoyatz.afse.wallet.core.api.config.interceptors.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import timber.log.Timber
import java.util.concurrent.TimeUnit
import gr.jvoyatz.afse.wallet.core.api.BuildConfig as api_R
/**
 * Contains the base configuration used for Retrofit ApiServices
 */
object ApiProvider {
    private const val TIMEOUT = 5L
    private const val TAG = "WalletApi"

    internal val okHttpClient by lazy {
        OkHttpClient.Builder().apply {

            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)

            addInterceptor(BasicAuthInterceptor(api_R.USERNAME, api_R.PASSWORD))
            addInterceptor(loggingInterceptor)
        }.run {
            build()
        }
    }

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor {
            Timber.tag(TAG).d(it)
        }.apply {
            level =
                if (api_R.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    private val moshi by lazy {
        with(Moshi.Builder()){
            addLast(KotlinJsonAdapterFactory())
        }.run {
            build()
        }
    }

    internal inline fun <reified T> getApi(): T = Retrofit.Builder().apply {
        baseUrl(api_R.HOST)
        addConverterFactory(MoshiConverterFactory.create(moshi))
        client(okHttpClient)
    }.let {
        it.build()
    }.run {
        create()
    }
}