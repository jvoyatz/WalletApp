package gr.jvoyatz.assignment.wallet.domain.repository

import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions

/**
 * A repository which defines the contract for getting access to the local stored accounts
 */
interface AccountsRepository {



    /**
     * Returns the accounts saved in the database
     */
    suspend fun getAccounts(): ResultData<List<Account>>

    /**
     * Makes the request to fetch details for a selected account
     */
    suspend fun getAccountDetails(id: String): ResultData<Account>

    /**
     * Executes a call to fetch transactions data for the selected account
     */
    suspend fun getAccountTransactions(id: String, page: Int, dateFrom: String? = null, dateTo: String? = null): ResultData<PagedTransactions>

    /**
     * sets an account as selected when user clicks on in portfolio screen,
     * keep it in this place in order not to need to make a new request
     */
    suspend fun setSelectedAccount(account: Account): ResultData<Unit>

    /**
     * Save an account as favorite in the database
     */
    suspend fun addFavoriteAccount(account: Account): ResultData<Unit>

    /**
     * Save an account as favorite in the database
     */
    suspend fun removeFavoriteAccount(account: Account): ResultData<Unit>
}