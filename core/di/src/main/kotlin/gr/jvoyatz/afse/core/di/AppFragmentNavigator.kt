package gr.jvoyatz.afse.core.di

import androidx.navigation.fragment.findNavController
import gr.jvoyatz.afse.core.navigation.Destination
import gr.jvoyatz.afse.core.navigation.Navigator
import timber.log.Timber

class AppFragmentNavigator: Navigator.FragmentNavigator() {
    override fun navigate(destination: Destination) {
        fragment?.findNavController()?.let {
            when(destination){
                Destination.AccountsPortfolio -> Timber.d("navigating to portfolio screen")
                Destination.AccountDetails -> Timber.d("navigating to account details screen")
            }
        }
    }
}