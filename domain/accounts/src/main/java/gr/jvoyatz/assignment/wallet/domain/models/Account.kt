package gr.jvoyatz.assignment.wallet.domain.models

/**
 * Account domain model
 */
data class Account(
    val id: String,
    val accountNickname: String?,
    val accountNumber: Int,
    val accountType: AccountType,
    val balance: String,
    val currencyCode: String,
    var isFavorite: Boolean = false
){
    var details: AccountDetails? = null
    var pagedTransactions: PagedTransactions? = null
}