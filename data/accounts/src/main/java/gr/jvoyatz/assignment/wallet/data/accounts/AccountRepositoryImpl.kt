package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.core.common.resultdata.asSuccess
import gr.jvoyatz.assignment.core.common.resultdata.isSuccess
import gr.jvoyatz.assignment.core.common.resultdata.mapSuccess
import gr.jvoyatz.assignment.core.common.resultdata.resultOf
import gr.jvoyatz.assignment.core.common.resultdata.suspendedResultOf
import gr.jvoyatz.assignment.core.common.utils.Utils
import gr.jvoyatz.assignment.core.database.AccountsDao
import gr.jvoyatz.assignment.wallet.core.api.WalletApi
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponseExt.asResult
import gr.jvoyatz.assignment.wallet.core.api.config.response.asSuccess
import gr.jvoyatz.assignment.wallet.core.api.config.response.isError
import gr.jvoyatz.assignment.wallet.core.api.config.response.isSuccess
import gr.jvoyatz.assignment.wallet.core.api.models.TransactionsPagedRequest
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.dtoToAccounts
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.toAccountEntity
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.toDomain
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.toPagedAccountTransactions
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.AccountDetails
import gr.jvoyatz.assignment.wallet.domain.models.AccountException
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository
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

    private var selectedAccount: Account? = null

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

    override suspend fun getAccountDetails2(id: String): ResultData<Account> = suspendedResultOf {
        val account = (selectedAccount) ?: throw AccountException("selectedAccount is null")
        if (account.id != id) throw AccountException("ids don't match")

        //1. check if this account exists in the db as favorite
        val dbAccount = dbSource.getAccountById(id).firstOrNull {it.id == id }

        Timber.d("dbaccount $dbAccount")

        //2.get details
        var details: AccountDetails? = null

        val detailsResponse = apiSource.getAccountDetails(id)
        if(detailsResponse.isError() && dbAccount == null) throw AccountException("not able to get account details")
        else if(detailsResponse.isError() && dbAccount != null){
            details = dbAccount.toDomain().details
        }else if(detailsResponse.isSuccess()){
            details = detailsResponse.asSuccess()!!.body.toDomain()
        }

        //3. get transactions
        val transactions = with(getAccountTransactions(id, 0)){
            if(isSuccess()) asSuccess()!!.data else null
        }

        account.apply {
            this.isFavorite = dbAccount != null
            this.details = details
            this.pagedTransactions = transactions
        }.also {
            if(detailsResponse.isSuccess() && dbAccount != null){
                dbSource.addFavoriteAccount(it.toAccountEntity())
            }
        }
    }

    override suspend fun getAccountDetails(id: String): ResultData<Account> =
        apiSource.getAccountDetails(id)
            .asResult()
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