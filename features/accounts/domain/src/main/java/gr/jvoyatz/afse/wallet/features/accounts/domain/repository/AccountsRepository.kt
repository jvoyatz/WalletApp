package gr.jvoyatz.afse.wallet.features.accounts.domain.repository

import gr.jvoyatz.afse.core.common.resultdata.ResultData
import gr.jvoyatz.afse.wallet.features.accounts.domain.models.Account

/**
 * A repository which defines the contract for getting access to the local stored accounts
 */
interface AccountsRepository {

    /**
     * Save an account as favorite in the database
     */
    suspend fun addFavoriteAccount(account: Account): ResultData<Unit>

    /**
     * Returns the accounts saved in the database
     */
    suspend fun getAccounts(): ResultData<List<Account>>
}