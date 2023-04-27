package gr.jvoyatz.assignment.wallet.features.accounts.domain.usecases

import gr.jvoyatz.assignment.wallet.features.accounts.domain.models.Account
import gr.jvoyatz.assignment.wallet.features.accounts.domain.repository.AccountsRepository

/**
 * Marks an account as favorite in the database
 */
class AddFavoriteAccountUseCase(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(account: Account) = repository.addFavoriteAccount(account)
}