package gr.jvoyatz.assignment.core.navigation

import androidx.navigation.NavController

interface Navigator {
     fun navigate(destination: Destination)
     fun bind(navController: NavController)
     fun unbind()
}


