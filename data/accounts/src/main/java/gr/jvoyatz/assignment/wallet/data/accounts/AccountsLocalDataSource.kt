@file:Suppress("unused")

package gr.jvoyatz.assignment.wallet.data.accounts

import gr.jvoyatz.assignment.core.database.AccountsDao
import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

internal class AccountsLocalDataSource(
     val dao: AccountsDao
) {
    suspend fun getAccountById(id: String) = dao.selectById(id)
    suspend fun isFavorite(id: String) = dao.exists(id)

    fun getAccounts(): Flow<List<AccountEntity>> = dao.getAccounts()

    suspend fun addFavoriteAccount(accountEntity: AccountEntity){
        dao.insertAccount(accountEntity)
    }
    suspend fun removeFavoriteAccount(accountEntity: AccountEntity){
        dao.deleteAccount(accountEntity)
    }

    suspend fun deleteAccounts(){
        dao.deleteAccounts()
    }
}