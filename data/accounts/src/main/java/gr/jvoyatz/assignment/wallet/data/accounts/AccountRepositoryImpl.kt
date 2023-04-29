package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.core.common.resultdata.mapSuccess
import gr.jvoyatz.assignment.core.database.AccountsDao
import gr.jvoyatz.assignment.wallet.core.api.WalletApi
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponseExt.asResult
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.dtoToAccounts
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository


/**
 * Implementation of the AccountRepository.
 *
 * It is responsible for returning the saved accounts as they have been found in the db.
 */


internal class AccountRepositoryImpl(
    private val apiSource: AccountsApiDataSource,
    private val dbSource: AccountsLocalDataSource
) : AccountsRepository {

    private var selectedAccount: Account?= null

    override suspend fun getAccounts(): ResultData<List<Account>> {
        return apiSource.getAccounts().asResult()
            .mapSuccess {
                dtoToAccounts()
            }
    }

    companion object {
        fun create(api: WalletApi, dao: AccountsDao): AccountsRepository{
            return AccountRepositoryImpl(
                AccountsApiDataSource(api),
                AccountsLocalDataSource(dao)
            )
        }
    }
}