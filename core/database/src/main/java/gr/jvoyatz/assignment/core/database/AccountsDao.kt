package gr.jvoyatz.assignment.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(accountEntity: AccountEntity)

    @Query("SELECT * from AccountEntity")
    fun getAccounts2(): List<AccountEntity>
    @Query("SELECT * from AccountEntity")
    fun getAccounts(): Flow<List<AccountEntity>>

    @Query("DELETE from AccountEntity")
    suspend fun deleteAccounts()

    @Delete
    suspend fun deleteAccount(accountEntity: AccountEntity)
}