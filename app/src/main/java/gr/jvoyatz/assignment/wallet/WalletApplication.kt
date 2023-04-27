package gr.jvoyatz.assignment.wallet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WalletApplication :Application(){
    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(gr.jvoyatz.assignment.wallet.TimberDebugTree())
        }
    }

}