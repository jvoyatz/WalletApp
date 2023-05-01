package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.core.testing.MockData
import gr.jvoyatz.assignment.wallet.core.api.ApiSource
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse
import gr.jvoyatz.assignment.wallet.core.api.models.AccountDetailsDto
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.assignment.wallet.core.api.models.AccountTransactionsDto
import gr.jvoyatz.assignment.wallet.core.api.models.TransactionsPagedRequest

class FakeApiSource: ApiSource {

    private val dto :MutableList<AccountRaw> = mutableListOf()

    private var apiResponse: ApiResponse<List<AccountRaw>, String>? = null
    fun setSuccessApiResponse(data: List<AccountRaw>){
        dto.clear()
        dto.addAll(data)
        apiResponse = ApiResponse.success(data)
    }

    fun setHttpErrorApiResponse(){
        apiResponse = ApiResponse.httpError(400, MockData.MOCK_NET_ERROR_RESPONSE)
    }

    fun setIoErrorApiResponse(){
        apiResponse = ApiResponse.httpError(500, MockData.MOCK_NET_ERROR_RESPONSE)
    }
    override suspend fun getAccounts(): ApiResponse<List<AccountRaw>, String> {
        return apiResponse!!
    }

    override suspend fun getAccountDetails(id: String): ApiResponse<AccountDetailsDto, String> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountTransactions(
        id: String,
        transactionsPagedRequest: TransactionsPagedRequest
    ): ApiResponse<AccountTransactionsDto, String> {
        TODO("Not yet implemented")
    }
}