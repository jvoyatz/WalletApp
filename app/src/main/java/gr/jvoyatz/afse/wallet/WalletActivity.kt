package gr.jvoyatz.afse.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gr.jvoyatz.afse.wallet.databinding.ActivityWalletBinding

class WalletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(gr.jvoyatz.afse.core.ui.R.style.Theme_AFSEWallet)
        with(ActivityWalletBinding.inflate(layoutInflater)){
            setContentView(this.root)
        }
    }
}