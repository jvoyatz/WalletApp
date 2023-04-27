package gr.jvoyatz.assignment.wallet.core.api

import android.content.Context
import gr.jvoyatz.assignment.wallet.core.api.models.AccountsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Defines the contract used to communicate with the Wallet Web Service
 */
interface WalletApi {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("accounts")
    suspend fun getAccounts(): Response<AccountsDto>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("accounts/details/{account_id}")
    suspend fun getAccountDetails(@Path("account_id") accountId: String = "1f34c76a-b3d1-43bc-af91-a82716f1bc2e")

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("accounts/transactions/{account_id}")
    suspend fun getAccountTransactions(@Path("account_id") accountId: String = "1f34c76a-b3d1-43bc-af91-a82716f1bc2e")

    companion object{
        fun create(context: Context) = ApiProvider.getApi<WalletApi>(context)
    }
}