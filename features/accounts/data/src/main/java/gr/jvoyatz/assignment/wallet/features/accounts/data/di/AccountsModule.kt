package gr.jvoyatz.assignment.wallet.features.accounts.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.jvoyatz.assignment.core.database.AccountsDao
import gr.jvoyatz.assignment.wallet.core.api.WalletApi
import gr.jvoyatz.assignment.wallet.features.accounts.data.AccountRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AccountsModule {

   // fun bindsAccountsRepository(impl: AccountRepositoryImpl): AccountsRepository

    @Singleton
    @Provides
    fun provideAccountsRepository(walletApi: WalletApi, dao: AccountsDao) =
        AccountRepositoryImpl.create(walletApi, dao)
}