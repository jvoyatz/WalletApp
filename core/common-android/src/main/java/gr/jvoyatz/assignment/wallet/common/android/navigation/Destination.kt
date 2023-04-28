package gr.jvoyatz.assignment.wallet.common.android.navigation

sealed interface Destination{
    object AccountsPortfolio: Destination
    class AccountDetails(val destination: Int): Destination
}
