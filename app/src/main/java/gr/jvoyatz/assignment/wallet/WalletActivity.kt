package gr.jvoyatz.assignment.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import gr.jvoyatz.assignment.wallet.common.android.navigation.Navigator

import gr.jvoyatz.assignment.wallet.databinding.ActivityWalletBinding
import javax.inject.Inject
import gr.jvoyatz.assignment.core.ui.R as ui_R

@AndroidEntryPoint
class WalletActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ui_R.style.Theme_Wallet)
        with(ActivityWalletBinding.inflate(layoutInflater)){
            setContentView(this.root)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    override fun onResume() {
        super.onResume()
        navigator.bind(findNavController(R.id.nav_host_fragment))
    }

    override fun onPause() {
        super.onPause()
        navigator.unbind()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }
}