package gr.jvoyatz.assignment.wallet.features.accounts.data.internal

import gr.jvoyatz.assignment.core.database.AccountsDao
import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

internal class AccountsLocalDataSource(
    private val dao: AccountsDao
) {

    fun getAccounts(): Flow<List<AccountEntity>> = dao.getAccounts()

    suspend fun addFavoriteAccount(accountEntity: AccountEntity){
        dao.insertAccount(accountEntity)
    }

    suspend fun deleteAccounts(){
        dao.deleteAccounts()
    }
    suspend fun deleteAccount(accountEntity: AccountEntity){
        dao.deleteAccount(accountEntity)
    }
}