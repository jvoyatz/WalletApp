package gr.jvoyatz.assignment.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import gr.jvoyatz.assignment.core.database.entities.AccountEntity

@Database(
    entities = [AccountEntity::class],
    version = 1
)
abstract class AccountsDatabase : RoomDatabase() {
    abstract val dao: AccountsDao
}