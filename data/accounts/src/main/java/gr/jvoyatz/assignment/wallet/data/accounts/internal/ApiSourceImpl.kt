package gr.jvoyatz.assignment.wallet.data.accounts.internal

import gr.jvoyatz.assignment.wallet.core.api.WalletApi
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse
import gr.jvoyatz.assignment.wallet.core.api.config.response.safeRawApiCall
import gr.jvoyatz.assignment.wallet.core.api.models.AccountDetailsDto
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.assignment.wallet.core.api.models.AccountTransactionsDto
import gr.jvoyatz.assignment.wallet.core.api.models.TransactionsPagedRequest
import gr.jvoyatz.assignment.wallet.core.api.ApiSource

internal class ApiSourceImpl(
    private val api: WalletApi
) : ApiSource {

    override suspend fun getAccounts(): ApiResponse<List<AccountRaw>, String> {
        return safeRawApiCall {
            api.getAccounts()
        }
    }

    override suspend fun getAccountDetails(id: String): ApiResponse<AccountDetailsDto, String> {
        return safeRawApiCall {
            api.getAccountDetails(id)
        }
    }

    override suspend fun getAccountTransactions(id: String, transactionsPagedRequest: TransactionsPagedRequest): ApiResponse<AccountTransactionsDto, String> {
        return safeRawApiCall {
            api.getAccountTransactions(id, transactionsPagedRequest)
        }
    }
}