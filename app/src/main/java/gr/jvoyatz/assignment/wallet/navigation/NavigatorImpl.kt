package gr.jvoyatz.assignment.wallet.navigation

import androidx.navigation.NavController
import gr.jvoyatz.assignment.wallet.accounts.AccountsFragmentDirections
import gr.jvoyatz.assignment.wallet.common.android.navigation.Destination
import gr.jvoyatz.assignment.wallet.common.android.navigation.Navigator
import javax.inject.Inject

/**
 * Navigator's interface implementation
 * Used to support implementation through diferrent feature modules.
 *
 * We can do the binding of app's NavController when activity (our single activity) starts.
 */
internal class NavigatorImpl @Inject constructor() : Navigator {

    private var navController: NavController? = null

    override fun navigate(destination: Destination) {
        navController?.let {
            when(destination){
                is Destination.AccountDetailsScreen -> {
                    navController?.let {
                        if(it.currentDestination?.id == gr.jvoyatz.assignment.wallet.features.account.details.R.id.account_details) return
                        it.navigate(AccountsFragmentDirections.actionAccountToDetails(destination.accountId))
                    }
                }
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