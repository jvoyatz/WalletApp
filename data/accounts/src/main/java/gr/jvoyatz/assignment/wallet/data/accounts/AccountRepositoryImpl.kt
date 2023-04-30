package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.core.common.resultdata.mapSuccess
import gr.jvoyatz.assignment.core.common.resultdata.resultOf
import gr.jvoyatz.assignment.core.common.resultdata.suspendedResultOf
import gr.jvoyatz.assignment.core.common.utils.Utils
import gr.jvoyatz.assignment.core.database.AccountsDao
import gr.jvoyatz.assignment.wallet.core.api.WalletApi
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponseExt.asResult
import gr.jvoyatz.assignment.wallet.core.api.models.TransactionsPagedRequest
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.dtoToAccounts
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.toAccountEntity
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.toDomain
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.toPagedAccountTransactions

import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.collect
import timber.log.Timber


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

    override suspend fun addFavoriteAccount(account: Account): ResultData<Unit> = suspendedResultOf {
        dbSource.addFavoriteAccount(account.toAccountEntity())
    }

    override suspend fun removeFavoriteAccount(account: Account): ResultData<Unit> = suspendedResultOf {
        dbSource.removeFavoriteAccount(account.toAccountEntity())
    }

    override suspend fun setSelectedAccount(account: Account): ResultData<Unit> {
        return resultOf {
            selectedAccount = account
        }
    }

    override suspend fun getAccountDetails(id: String): ResultData<Account> =
        apiSource.getAccountDetails(id).asResult()
            .mapSuccess {
                val accountDetailsDomain = this.toDomain()
                selectedAccount!!.apply {
                    this.details = accountDetailsDomain
                }
            }

    override suspend fun getAccountTransactions(
        id: String,
        page: Int,
        dateFrom: String?,
        dateTo: String?
    ) : ResultData<PagedTransactions>{
        return apiSource.getAccountTransactions(id, TransactionsPagedRequest(nextPage = page)).asResult()
            .mapSuccess {
                val currency = selectedAccount?.currencyCode?.let { Utils.getCurrency(it) } ?: ""
                this.toPagedAccountTransactions(currency)
            }
    }


    companion object {

        /**
         * Creates a new Wallet Repository using the needed data sources
         */
        internal fun create(api: WalletApi, dao: AccountsDao): AccountsRepository{
            return AccountRepositoryImpl(
                AccountsApiDataSource(api),
                AccountsLocalDataSource(dao)
            )
        }
    }
}