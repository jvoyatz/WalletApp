package gr.jvoyatz.assignment.wallet.domain.usecases

import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository

/**
 * Returns the accounts of this user as fetched by the remote service.
 * However if one of those accounts is stored as favorite in the local db,
 * then it will be placed first in the list.
 */
class GetAccountsUseCase(
    private val accountsRepository: AccountsRepository
){
    suspend operator fun invoke() = accountsRepository.getAccounts()
}