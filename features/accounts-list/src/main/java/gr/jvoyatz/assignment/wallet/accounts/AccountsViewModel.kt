package gr.jvoyatz.assignment.wallet.accounts

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.jvoyatz.assignment.core.common.resultdata.onError
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedError
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedSuccess
import gr.jvoyatz.assignment.core.mvvmi.BaseViewModel
import gr.jvoyatz.assignment.wallet.accounts.Contract.Reduce

import gr.jvoyatz.assignment.wallet.common.android.AppDispatchers
import gr.jvoyatz.assignment.wallet.common.android.ui.mappers.toUiModel
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.usecases.GetAccountsUseCase
import gr.jvoyatz.assignment.wallet.domain.usecases.UseCases
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val setSelectedAccountUseCase: UseCases.SetSelectedAccountUseCase,
    private val appDispatchers: AppDispatchers
): BaseViewModel<Contract.State, Reduce, Contract.Intent, Contract.Event>(
    savedStateHandle, Contract.State(Contract.ViewState.Initialize)
) {
    override fun mapIntents(intent: Contract.Intent): Flow<Reduce> {
        return when(intent){
            Contract.Intent.Initialize -> getAccounts()
            Contract.Intent.GetData -> getAccounts()
            is Contract.Intent.OnAccountSelected -> selectAccount(intent.account)
        }
    }

    override fun reduceUiState(
        state: Contract.State,
        reduce: Reduce
    ): Contract.State {
        val newState = if (reduce is Reduce.Loading) {
            Contract.ViewState.Loading
        } else if (reduce is Reduce.Error) {
            Contract.ViewState.Error
        } else if ((reduce is Reduce.Data) && !(reduce.data.isNullOrEmpty())) {
            Contract.ViewState.Data(reduce.data.map { it.toUiModel() })
        } else if (reduce is Reduce.Data && reduce.data.isNullOrEmpty()) {
            Contract.ViewState.NoData
        } else {
            Contract.ViewState.Initialize
        }
        return state.copy(state = newState)
    }

    private fun getAccounts():Flow<Reduce> = flow {
        emit(Reduce.Loading)
        delay(500)
        val result = getAccountsUseCase()
        result.onSuspendedError {
            emit(Reduce.Error)
        }.onSuspendedSuccess {
            emit(Reduce.Data(data = it))
        }
    }.flowOn(appDispatchers.default)

    private fun selectAccount(account: Account) = flow<Reduce> {
        setSelectedAccountUseCase(account)
            .onSuspendedSuccess {
                postEvent { Contract.Event.AccountDetailsNavigation(account.id) }
            }.onError {
                postEvent { Contract.Event.ShowToast(gr.jvoyatz.assignment.core.ui.R.string.an_error_occurred) }
            }
    }.flowOn(appDispatchers.default)
}
