package gr.jvoyatz.assignment.core.common.utils

import java.util.Currency


object Utils {
    private val currenciesMap: Map<String, String> = mapOf(
        "EUR" to "€",
        "USD" to "$",
        "GBP" to "£"
    )


    private val fallbackBlock: (String) -> String = { text ->
        currenciesMap[text] ?: text
    }

    fun getCurrency(currency: String): String = try {
        Currency.getInstance(currency).symbol
    } catch (e: Exception) {
        e.printStackTrace()
        fallbackBlock(currency)
    }
}