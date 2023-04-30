package gr.jvoyatz.assignment.wallet.core.api

import android.content.Context
import gr.jvoyatz.assignment.wallet.core.api.models.AccountDetailsDto
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.assignment.wallet.core.api.models.AccountTransactionsDto
import gr.jvoyatz.assignment.wallet.core.api.models.AccountsDto
import gr.jvoyatz.assignment.wallet.core.api.models.TransactionsPagedRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Defines the contract used to communicate with the Wallet Web Service
 */
interface WalletApi {

    @GET("accounts")
    suspend fun getAccounts(): List<AccountRaw>


    @GET("account/details/{account_id}")
    suspend fun getAccountDetails(@Path("account_id") accountId: String): AccountDetailsDto


    @POST("account/transactions/{account_id}")
    suspend fun getAccountTransactions(@Path("account_id") accountId: String, @Body body: TransactionsPagedRequest): AccountTransactionsDto

    companion object{
        internal fun create(context: Context) = ApiProvider.getApi<WalletApi>(context)
    }
}