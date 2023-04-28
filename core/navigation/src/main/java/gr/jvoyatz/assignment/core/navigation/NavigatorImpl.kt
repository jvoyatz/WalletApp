package gr.jvoyatz.assignment.core.navigation

import androidx.navigation.NavController
import timber.log.Timber
import javax.inject.Inject

internal class NavigatorImpl @Inject constructor(

): Navigator {

    private var navController: NavController? = null

    override fun navigate(destination: Destination) {
        navController?.let {
            when(destination){
                Destination.AccountsPortfolio -> Timber.d("navigating to portfolio screen")
                Destination.AccountDetails -> Timber.d("navigating to account details screen")
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