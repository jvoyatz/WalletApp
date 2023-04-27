package gr.jvoyatz.afse.wallet.features.accounts.domain.usecases

import gr.jvoyatz.afse.wallet.features.accounts.domain.models.Account
import gr.jvoyatz.afse.wallet.features.accounts.domain.repository.AccountsRepository

/**
 * Marks an account as favorite in the database
 */
class AddFavoriteAccountUseCase(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(account: Account) = repository.addFavoriteAccount(account)
}