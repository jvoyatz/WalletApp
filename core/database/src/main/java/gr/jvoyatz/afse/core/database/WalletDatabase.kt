package gr.jvoyatz.afse.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import gr.jvoyatz.afse.core.database.entities.AccountEntity

@Database(
    entities = [AccountEntity::class],
    version = 1
)
abstract class WalletDatabase : RoomDatabase() {
    abstract val dao: WalletDao
}