package gr.jvoyatz.assignment.wallet.accounts

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedError
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedSuccess
import gr.jvoyatz.assignment.core.mvvmi.BaseViewModel
import gr.jvoyatz.assignment.wallet.accounts.AccountsContract.ScreenState.Partial
import gr.jvoyatz.assignment.wallet.accounts.mapper.toUiModel
import gr.jvoyatz.assignment.wallet.domain.usecases.GetAccountsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
     savedStateHandle: SavedStateHandle,
    private val getAccountsUseCase: GetAccountsUseCase
): BaseViewModel<AccountsContract.State, Partial, AccountsContract.Intent, AccountsContract.Event>(
    savedStateHandle, AccountsContract.State(AccountsContract.ScreenState.Initialize)
) {
    override fun mapIntents(intent: AccountsContract.Intent): Flow<Partial> {
        return when(intent){
            AccountsContract.Intent.Initialize -> {
                Timber.d("initialize get data")
                return emptyFlow()
            }
            AccountsContract.Intent.GetData -> return getAccounts()
            is AccountsContract.Intent.OnAccountSelected -> {
                Timber.d("on account selected ${intent.account}")
                return emptyFlow()
            }
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
            } else {
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
            Timber.d("onSuccess $it")
            emit(Partial(isSuccess = true, data = it))
        }
    }
}
