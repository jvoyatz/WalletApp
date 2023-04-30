package gr.jvoyatz.assignment.wallet.domain.usecases

import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions

object UseCases {
    fun interface SetSelectedAccountUseCase: suspend (Account) -> ResultData<Unit>

    fun interface GetAccountDetailsUseCase: suspend (String) -> ResultData<Account>

    fun interface GetAccountTransactionsUserCase: suspend (String, Int, String?, String?) -> ResultData<PagedTransactions>
}