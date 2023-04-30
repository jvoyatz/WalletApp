package gr.jvoyatz.assignment.wallet.domain.models

/**
 * Wallet Account Types
 */
enum class AccountType(val type: String) {
    CURRENT("Current"),
    SAVINGS("Savings"),
    TIME("Time"),
    CREDIT_CARD("Credit card"),
    NONE(" - ");


    companion object {
        private val typesMap = AccountType.values().associateBy { it.type.lowercase() }
        operator fun get(type: String?) = this from type
        infix fun from(type: String?) = type?.let { typesMap[it] } ?: NONE
    }
}

