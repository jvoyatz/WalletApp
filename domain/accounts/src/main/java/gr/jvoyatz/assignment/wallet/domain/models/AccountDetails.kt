package gr.jvoyatz.assignment.wallet.domain.models

/**
 * Contains further details for an account,
 * included in [Account]
 */
data class AccountDetails(
    val beneficiaries: String,
    val branch: String,
    val openedDate: String,
    val productName: String
)