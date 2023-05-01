package gr.jvoyatz.assignment.core.database

import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

internal interface DbSource {
    suspend fun getAccountById(id: String): List<AccountEntity>

    suspend fun isFavorite(id: String): Boolean
    fun getAccounts(): Flow<List<AccountEntity>>

    suspend fun addFavoriteAccount(accountEntity: AccountEntity)
    suspend fun removeFavoriteAccount(accountEntity: AccountEntity)
    suspend fun deleteAccounts()
}