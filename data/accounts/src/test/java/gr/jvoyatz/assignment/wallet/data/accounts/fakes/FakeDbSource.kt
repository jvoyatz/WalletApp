package gr.jvoyatz.assignment.wallet.data.accounts.fakes

import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import gr.jvoyatz.assignment.wallet.data.accounts.DbSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeDbSource: DbSource{

    private val flow: MutableSharedFlow<List<AccountEntity>> = MutableSharedFlow(replay = 1)

    suspend fun emit(entities: List<AccountEntity>){
        flow.emit(entities)
    }
    override suspend fun getAccountById(id: String): List<AccountEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun isFavorite(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAccounts(): Flow<List<AccountEntity>> = flow

    override suspend fun addFavoriteAccount(accountEntity: AccountEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavoriteAccount(accountEntity: AccountEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccounts() {
        TODO("Not yet implemented")
    }

}