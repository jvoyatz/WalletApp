package gr.jvoyatz.assignment.wallet.domain.usecases

import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns the accounts of this user as fetched by the remote service.
 * However if one of those accounts is stored as favorite in the local db,
 * then it will be placed first in the list.
 */
class GetAccountsUseCaseImpl(
    private val accountsRepository: AccountsRepository
): GetAccountsUseCase {
    /*suspend*/ override operator fun invoke(refresh: Boolean) = accountsRepository.getAccounts(refresh)
}
interface GetAccountsUseCase{
    operator fun invoke(refresh: Boolean = false): Flow<ResultData<List<Account>>>
}