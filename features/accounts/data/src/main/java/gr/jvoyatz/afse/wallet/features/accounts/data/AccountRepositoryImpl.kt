package gr.jvoyatz.afse.wallet.features.accounts.data

import gr.jvoyatz.afse.core.common.resultdata.ResultData
import gr.jvoyatz.afse.wallet.features.accounts.data.internal.AccountsApiDataSource
import gr.jvoyatz.afse.wallet.features.accounts.data.internal.AccountsDbDataSource
import gr.jvoyatz.afse.wallet.features.accounts.domain.models.Account
import gr.jvoyatz.afse.wallet.features.accounts.domain.repository.AccountsRepository
import javax.inject.Inject

/**
 * Implementation of the AccountRepository.
 *
 * It is responsible for returning the saved accounts as they have been found in the db.
 */
class AccountRepositoryImpl @Inject constructor(
    private val dbSource: AccountsDbDataSource,
    private val apiSource: AccountsApiDataSource
) : AccountsRepository {
    override suspend fun addFavoriteAccount(account: Account): ResultData<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccounts(): ResultData<List<Account>> {
        TODO("Not yet implemented")
    }
}