package gr.jvoyatz.assignment.wallet.domain.repository

import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions

/**
 * A repository which defines the contract for getting access to the local stored accounts
 */
interface AccountsRepository {

//    /**
//     * Save an account as favorite in the database
//     */
//    suspend fun addFavoriteAccount(account: Account): ResultData<Unit>

    /**
     * Returns the accounts saved in the database
     */
    suspend fun getAccounts(): ResultData<List<Account>>

    suspend fun setSelectedAccount(account: Account): ResultData<Unit>

    suspend fun getAccountDetails(id: String): ResultData<Account>

    suspend fun getAccountTransactions(id: String, page: Int, dateFrom: String? = null, dateTo: String? = null): ResultData<PagedTransactions>
}