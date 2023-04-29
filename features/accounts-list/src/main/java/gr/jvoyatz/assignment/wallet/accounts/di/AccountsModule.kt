package gr.jvoyatz.assignment.wallet.accounts.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository
import gr.jvoyatz.assignment.wallet.domain.usecases.GetAccountsUseCase


@InstallIn(SingletonComponent::class)
@Module
object
AccountsModule {
    @Provides
    fun provideGetAccountsUseCase(accountsRepository: AccountsRepository) = GetAccountsUseCase(accountsRepository)
}