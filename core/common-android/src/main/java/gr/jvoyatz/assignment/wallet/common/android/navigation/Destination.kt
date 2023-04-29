package gr.jvoyatz.assignment.wallet.common.android.navigation

sealed interface Destination{
    class AccountDetailsScreen(val accountId: String): Destination
}
