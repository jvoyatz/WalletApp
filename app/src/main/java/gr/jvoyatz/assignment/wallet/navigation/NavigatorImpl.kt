package gr.jvoyatz.assignment.wallet.navigation

import androidx.navigation.NavController
import gr.jvoyatz.assignment.wallet.common.android.navigation.Navigator
import gr.jvoyatz.assignment.wallet.features.accounts.ui.AccountsFragmentDirections
//import gr.jvoyatz.assignment.wallet.features.accounts.ui.AccountsFragmentDirections
import timber.log.Timber
import javax.inject.Inject

/**
 * Navigator's interface implementation
 * Used to support implementation through diferrent feature modules.
 *
 * We can do the binding of app's NavController when activity (our single activity) starts.
 */
internal class NavigatorImpl @Inject constructor() : Navigator {

    private var navController: NavController? = null

    override fun navigate(destination: gr.jvoyatz.assignment.wallet.common.android.navigation.Destination) {
        navController?.let {
            when(destination){
                gr.jvoyatz.assignment.wallet.common.android.navigation.Destination.AccountsPortfolio -> Timber.d("navigating to portfolio screen")
                is gr.jvoyatz.assignment.wallet.common.android.navigation.Destination.AccountDetails -> navController?.navigate(
                    AccountsFragmentDirections.actionAccountToDetails(1))
            }
        }
    }

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        navController = null
    }
}