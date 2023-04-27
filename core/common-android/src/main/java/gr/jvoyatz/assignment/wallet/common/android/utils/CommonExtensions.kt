@file:Suppress("DEPRECATION")

package gr.jvoyatz.assignment.wallet.common.android.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Looper




fun Context.isConnected() = kotlin.run {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = connectivityManager.activeNetworkInfo
     netInfo != null && netInfo.isConnected
}

val Context.isOnline: Boolean
    get() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null) {
                    return networkInfo.isConnected && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE ||
                            networkInfo.type == ConnectivityManager.TYPE_VPN || networkInfo.type == ConnectivityManager.TYPE_ETHERNET)
                }
            } else {
                val network = cm.activeNetwork

                if (network != null) {
                    val nc = cm.getNetworkCapabilities(network)
                    return if (nc == null) {
                        false
                    } else {
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) /*||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)*/
                    }
                }
            }
        }
        return false
    }



/**
 * Check if current thread is Main Thread.
 */
inline val isMainThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()

/**
 * Get class name for a particular class
 */
fun <T: Any> T.TAG() = this.javaClass.simpleName