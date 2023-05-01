package gr.jvoyatz.assignment.wallet.accounts.fakes

import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.AccountType
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HiltFakeAccountRepository: AccountsRepository {
    override fun getAccounts(refresh: Boolean): Flow<gr.jvoyatz.assignment.core.common.resultdata.ResultData<List<Account>>> = flow {
        val list = listOf<Account>(
            Account("id1", "nickname1", 12345, AccountType.CREDIT_CARD, "10.0", "EUR", true)
        )
        emit(ResultData.success(list))
    }

    override suspend fun getRemoteAccounts() {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountDetails(id: String): gr.jvoyatz.assignment.core.common.resultdata.ResultData<Account> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountTransactions(
        id: String,
        page: Int,
        dateFrom: String?,
        dateTo: String?
    ): gr.jvoyatz.assignment.core.common.resultdata.ResultData<PagedTransactions> {
        TODO("Not yet implemented")
    }

    override suspend fun setSelectedAccount(account: Account): gr.jvoyatz.assignment.core.common.resultdata.ResultData<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addFavoriteAccount(account: Account): gr.jvoyatz.assignment.core.common.resultdata.ResultData<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavoriteAccount(account: Account): gr.jvoyatz.assignment.core.common.resultdata.ResultData<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavoriteAccounts(): gr.jvoyatz.assignment.core.common.resultdata.ResultData<Unit> {
        TODO("Not yet implemented")
    }
}