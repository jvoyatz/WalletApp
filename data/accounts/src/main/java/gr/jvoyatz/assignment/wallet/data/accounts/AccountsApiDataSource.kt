package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.wallet.core.api.WalletApi
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse
import gr.jvoyatz.assignment.wallet.core.api.config.response.safeRawApiCall
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw

internal class AccountsApiDataSource(
    private val api: WalletApi
) {

    suspend fun getAccounts(): ApiResponse<List<AccountRaw>, String> {
        return safeRawApiCall {
            api.getAccounts()
        }
    }
}