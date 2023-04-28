package gr.jvoyatz.assignment.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import timber.log.Timber
import java.util.concurrent.Executor

object DatabaseProvider {
    private const val TAG = "DATABASE"
    private const val MSG = "query: %s | args: %s"
    private const val DATABASE = "WALLET_DATABASE"

    private fun getDatabase(context: Context, executor: Executor): AccountsDatabase {
        return Room.databaseBuilder(
            context,
            AccountsDatabase::class.java,
            DATABASE
        ).apply {
            setQueryCallback(object : RoomDatabase.QueryCallback {
                override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                    Timber.tag(TAG).d(MSG, sqlQuery, bindArgs)
                }
            }, executor)
        }.build()
    }

    internal fun getWalletDao(context: Context, executor: Executor) = getDatabase(context, executor).dao
}