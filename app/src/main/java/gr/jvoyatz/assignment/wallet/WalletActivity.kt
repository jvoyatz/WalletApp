package gr.jvoyatz.assignment.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

import gr.jvoyatz.assignment.core.navigation.NavigatorProvider
import gr.jvoyatz.assignment.wallet.databinding.ActivityWalletBinding
import timber.log.Timber

import javax.inject.Inject
import gr.jvoyatz.assignment.core.ui.R as ui_R

@AndroidEntryPoint
class WalletActivity : AppCompatActivity() {

    @Inject
    lateinit var navigatorProvider: NavigatorProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ui_R.style.Theme_Wallet)
        with(ActivityWalletBinding.inflate(layoutInflater)){
            setContentView(this.root)
        }


        println("navigator provider ${navigatorProvider.fragmentNavigator}")
    }
}