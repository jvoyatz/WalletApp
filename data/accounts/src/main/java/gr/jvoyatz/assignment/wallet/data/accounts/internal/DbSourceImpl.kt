@file:Suppress("unused")

package gr.jvoyatz.assignment.wallet.data.accounts.internal

import gr.jvoyatz.assignment.core.database.AccountsDao
import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import gr.jvoyatz.assignment.wallet.data.accounts.DbSource
import kotlinx.coroutines.flow.Flow

internal class DbSourceImpl(
    private val dao: AccountsDao
) : DbSource {

    override suspend fun getAccountById(id: String) = dao.selectById(id)
    override suspend fun isFavorite(id: String) = dao.exists(id)

    override fun getAccounts(): Flow<List<AccountEntity>> = dao.getAccounts()

    override suspend fun addFavoriteAccount(accountEntity: AccountEntity) {
        dao.insertAccount(accountEntity)
    }

    override suspend fun removeFavoriteAccount(accountEntity: AccountEntity) {
        dao.deleteAccount(accountEntity)
    }

    override suspend fun deleteAccounts() {
        dao.deleteAccounts()
    }
}