package gr.jvoyatz.assignment.wallet.common.android.domain.models

/**
 * Wallet Account Types
 */
enum class AccountType(val type: String) {
    CURRENT("current"),
    SAVINGS("savings"),
    TIME("time"),
    CREDIT_CARD("credit card"),
    NONE(" - ");


    companion object {
        private val typesMap = AccountType.values().associateBy { it.type.lowercase() }
        operator fun get(type: String?) = this from type
        infix fun from(type: String?) = type?.let { typesMap[it] } ?: NONE
    }
}

