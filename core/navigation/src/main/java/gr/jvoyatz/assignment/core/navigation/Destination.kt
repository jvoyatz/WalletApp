package gr.jvoyatz.assignment.core.navigation

sealed interface Destination{
    object AccountsPortfolio: Destination
    object AccountDetails: Destination
}
