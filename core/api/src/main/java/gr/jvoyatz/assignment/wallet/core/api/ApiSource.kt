package gr.jvoyatz.assignment.wallet.core.api

import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse
import gr.jvoyatz.assignment.wallet.core.api.config.response.safeRawApiCall
import gr.jvoyatz.assignment.wallet.core.api.models.AccountDetailsDto
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.assignment.wallet.core.api.models.AccountTransactionsDto
import gr.jvoyatz.assignment.wallet.core.api.models.TransactionsPagedRequest

interface ApiSource {
    suspend fun getAccounts(): ApiResponse<List<AccountRaw>, String>

    suspend fun getAccountDetails(id: String): ApiResponse<AccountDetailsDto, String>

    suspend fun getAccountTransactions(
        id: String,
        transactionsPagedRequest: TransactionsPagedRequest
    ): ApiResponse<AccountTransactionsDto, String>
}