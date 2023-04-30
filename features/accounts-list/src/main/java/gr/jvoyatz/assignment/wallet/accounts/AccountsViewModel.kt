package gr.jvoyatz.assignment.wallet.accounts

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.jvoyatz.assignment.core.common.resultdata.onError
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedError
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedSuccess
import gr.jvoyatz.assignment.core.mvvmi.BaseViewModel
import gr.jvoyatz.assignment.wallet.accounts.AccountsContract.ScreenState.Partial
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
): BaseViewModel<AccountsContract.State, Partial, AccountsContract.Intent, AccountsContract.Event>(
    savedStateHandle, AccountsContract.State(AccountsContract.ScreenState.Initialize)
) {
    override fun mapIntents(intent: AccountsContract.Intent): Flow<Partial> {
        return when(intent){
            AccountsContract.Intent.Initialize -> getAccounts()
            AccountsContract.Intent.GetData -> getAccounts()
            is AccountsContract.Intent.OnAccountSelected -> selectAccount(intent.account)
        }
    }

    override fun reduceUiState(
        currentUiState: AccountsContract.State,
        partialUiState: Partial
    ): AccountsContract.State {
        var state = if (partialUiState.isLoading) {
                AccountsContract.ScreenState.Loading
            } else if (partialUiState.isError) {
                AccountsContract.ScreenState.Error
            } else if (partialUiState.isSuccess && !partialUiState.data.isNullOrEmpty()) {
                AccountsContract.ScreenState.Data(partialUiState.data!!.map { it.toUiModel() })
            } else if (partialUiState.isSuccess && partialUiState.data.isNullOrEmpty()) {
                AccountsContract.ScreenState.NoData
            }else {
                AccountsContract.ScreenState.Initialize
            }
        return currentUiState.copy(state = state)
    }

    private fun getAccounts():Flow<Partial> = flow {
        emit(Partial(isLoading = true))
        delay(500)
        val result = getAccountsUseCase()
        result.onSuspendedError {
            emit(Partial(isError = true))
        }.onSuspendedSuccess {
            emit(Partial(isSuccess = true, data = it))
        }
    }.flowOn(appDispatchers.default)

    private fun selectAccount(account: Account) = flow<Partial> {
        setSelectedAccountUseCase(account)
            .onSuspendedSuccess {
                postEvent { AccountsContract.Event.AccountDetailsNavigation(account.id) }
            }.onError {
                postEvent { AccountsContract.Event.ShowToast(gr.jvoyatz.assignment.core.ui.R.string.an_error_occurred) }
            }
    }.flowOn(appDispatchers.default)
}
