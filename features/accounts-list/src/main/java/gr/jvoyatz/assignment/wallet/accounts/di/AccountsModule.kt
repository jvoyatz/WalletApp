package gr.jvoyatz.assignment.wallet.accounts.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository
import gr.jvoyatz.assignment.wallet.domain.usecases.GetAccountsUseCase
import gr.jvoyatz.assignment.wallet.domain.usecases.UseCases


@InstallIn(SingletonComponent::class)
@Module
object
AccountsModule {
    @Provides
    fun provideGetAccountsUseCase(accountsRepository: AccountsRepository) = GetAccountsUseCase(accountsRepository)

    @Provides
    fun provideSetSelectedAccountUseCase(accountsRepository: AccountsRepository) =
        UseCases.SetSelectedAccountUseCase { account ->
            accountsRepository.setSelectedAccount(account)
        }

    @Provides
    fun provideAddFavoriteAccountUseCase(accountsRepository: AccountsRepository) =
        UseCases.AddFavoriteAccountUseCase {
            accountsRepository.deleteFavoriteAccounts()
            accountsRepository.addFavoriteAccount(it)
        }

    @Provides
    fun provideRemoveFavoriteAccountUseCase(accountsRepository: AccountsRepository) =
        UseCases.RemoveFavoriteAccountUseCase {
            accountsRepository.removeFavoriteAccount(it)
        }


}