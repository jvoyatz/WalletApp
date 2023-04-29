package gr.jvoyatz.assignment.wallet.features.account.details.domain.models

/**
 * Contains further details for an account,
 * included in [Account]
 */
data class AccountDetails(
    val beneficiaries: List<String>,
    val branch: String,
    val openedDate: String,
    val productName: String
)