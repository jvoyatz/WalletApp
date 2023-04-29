package gr.jvoyatz.assignment.wallet.features.accounts.ui.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.jvoyatz.assignment.wallet.features.accounts.domain.repository.AccountsRepository
import gr.jvoyatz.assignment.wallet.features.accounts.domain.usecases.GetAccountsUseCase

@InstallIn(SingletonComponent::class)
@Module
object
AccountsModule {
    @Provides
    fun provideGetAccountsUseCase(accountsRepository: AccountsRepository) = GetAccountsUseCase(accountsRepository)
}