package gr.jvoyatz.assignment.wallet.features.account.details.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository
import gr.jvoyatz.assignment.wallet.domain.usecases.UseCases

@InstallIn(SingletonComponent::class)
@Module
object AccountDetailsModule {
    @Provides
    fun provideGetAccountDetailsUseCase(repository: AccountsRepository) =
        UseCases.GetAccountDetailsUseCase {
            repository.getAccountDetails2(it)
        }

    @Provides
    fun providesGetAccountTransactionsUseCase(repository: AccountsRepository) =
        UseCases.GetAccountTransactionsUserCase { id, page, dateFrom, dateTo ->
            repository.getAccountTransactions(
                id = id, page = page, dateFrom = dateFrom, dateTo = dateTo
            )
        }
}