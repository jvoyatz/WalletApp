package gr.jvoyatz.assignment.wallet.common.android.navigation

import androidx.navigation.NavController

interface Navigator {
     fun navigate(destination: Destination)
     fun bind(navController: NavController)
     fun unbind()
}


