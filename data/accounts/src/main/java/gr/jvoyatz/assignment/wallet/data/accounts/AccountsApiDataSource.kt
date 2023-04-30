package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.wallet.core.api.WalletApi
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse
import gr.jvoyatz.assignment.wallet.core.api.config.response.safeRawApiCall
import gr.jvoyatz.assignment.wallet.core.api.models.AccountDetailsDto
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.assignment.wallet.core.api.models.AccountTransactionsDto
import gr.jvoyatz.assignment.wallet.core.api.models.TransactionsPagedRequest

internal class AccountsApiDataSource(
    private val api: WalletApi
) {

    suspend fun getAccounts(): ApiResponse<List<AccountRaw>, String> {
        return safeRawApiCall {
            api.getAccounts()
        }
    }

    suspend fun getAccountDetails(id: String): ApiResponse<AccountDetailsDto, String> {
        return safeRawApiCall {
            api.getAccountDetails(id)
        }
    }

    suspend fun getAccountTransactions(id: String, transactionsPagedRequest: TransactionsPagedRequest): ApiResponse<AccountTransactionsDto, String> {
        return safeRawApiCall {
            api.getAccountTransactions(id, transactionsPagedRequest)
        }
    }
}