package gr.jvoyatz.assignment.wallet.features.accounts.domain.repository

import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.wallet.features.accounts.domain.models.Account
import kotlinx.coroutines.flow.Flow

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
}