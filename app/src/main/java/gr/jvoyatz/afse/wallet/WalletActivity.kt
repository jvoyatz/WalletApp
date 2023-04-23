package gr.jvoyatz.afse.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import gr.jvoyatz.afse.core.navigation.NavigatorProvider
import gr.jvoyatz.afse.wallet.databinding.ActivityWalletBinding
import javax.inject.Inject
import gr.jvoyatz.afse.core.ui.R as ui_R

@AndroidEntryPoint
class WalletActivity : AppCompatActivity() {

    @Inject
    lateinit var navigatorProvider: NavigatorProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ui_R.style.Theme_AFSEWallet)
        with(ActivityWalletBinding.inflate(layoutInflater)){
            setContentView(this.root)
        }

        println("navigator provider ${navigatorProvider.fragmentNavigator}")
    }
}