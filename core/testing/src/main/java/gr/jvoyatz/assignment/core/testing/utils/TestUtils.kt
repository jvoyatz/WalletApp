package gr.jvoyatz.assignment.core.testing.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient

object TestUtils {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val okHttpClient = OkHttpClient.Builder().build()
    /**
     * For internal use only, loads the content of files located in the resources directory.
     *
     * for a file file.json, pass "/sport_events.json"
     */
    fun Any.loadResourceFile(fileName: String): String = this.javaClass.getResourceAsStream(fileName)?.bufferedReader().use { it!!.readText() }

    inline fun <reified T> deserializeList(content: String): List<T> {
        val type = com.squareup.moshi.Types.newParameterizedType(List::class.java, T::class.java)
        val moshi =  moshi
        return moshi.adapter<List<T>>(type).fromJson(content).orEmpty()
    }

    inline fun <reified T, R> getData(file: String, mapper: T.() -> R) : List<R> {
        return loadResourceFile(file).run {
            val list = deserializeList<T>(this)
            list!!.map(mapper)
        }
    }
}