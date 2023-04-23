package gr.jvoyatz.afse.core.navigation

sealed interface Destination{
    object AccountsPortfolio: Destination
    object AccountDetails: Destination
}
