package gr.jvoyatz.assignment.wallet.accounts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.jvoyatz.assignment.core.common.resultdata.onError
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedError
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedSuccess
import gr.jvoyatz.assignment.core.mvvmi.BaseViewModel
import gr.jvoyatz.assignment.core.ui.R
import gr.jvoyatz.assignment.wallet.accounts.Contract.Reduce
import gr.jvoyatz.assignment.wallet.common.android.AppDispatchers
import gr.jvoyatz.assignment.wallet.common.android.ui.mappers.toUiModel
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.usecases.CommonUseCases
import gr.jvoyatz.assignment.wallet.domain.usecases.GetAccountsUseCase
import gr.jvoyatz.assignment.wallet.domain.usecases.UseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val setSelectedAccountUseCase: UseCases.SetSelectedAccountUseCase,
    private val appDispatchers: AppDispatchers,
    private val commonUseCases: CommonUseCases,
): BaseViewModel<Contract.State, Reduce, Contract.Intent, Contract.Event>(
    savedStateHandle, Contract.State(Contract.ViewState.Initialize)
) {
    override fun mapIntents(intent: Contract.Intent): Flow<Reduce> {
        return when(intent){
            Contract.Intent.Initialize -> getAccounts()
            is Contract.Intent.GetData -> getAccounts(intent.refresh)
            is Contract.Intent.OnAccountSelected -> selectAccount(intent.account)
            is Contract.Intent.OnFavoriteAccount -> handleFavoriteAccountIntent(intent.account)
        }
    }

    override fun reduceUiState(
        state: Contract.State,
        reduce: Reduce
    ): Contract.State {
        val newState = when (reduce) {
            is Reduce.Data -> Contract.ViewState.Data(reduce.data.map { it.toUiModel() })
            is Reduce.NoData -> Contract.ViewState.NoData
            is Reduce.Loading ->  Contract.ViewState.Loading
            is Reduce.Error -> Contract.ViewState.Error
        }
        return state.copy(state = newState)
    }

    private fun getAccounts(refresh: Boolean = false): Flow<Reduce> = flow {
        getAccountsUseCase(refresh)
            .onStart { emit(Reduce.Loading )}
            .onEach { kotlinx.coroutines.delay(500) }
            .collect { result ->
                result.onSuspendedSuccess {
                    val state = if (it.isEmpty()) {
                        Reduce.NoData
                    } else {
                        Reduce.Data(it)
                    }
                    emit(state)
                }.onSuspendedError {
                    emit(Reduce.Error)
                }
            }
    }.flowOn(appDispatchers.default)

    private fun selectAccount(account: Account) = flow<Reduce> {
        setSelectedAccountUseCase(account)
            .onSuspendedSuccess {
                postEvent { Contract.Event.AccountDetailsNavigation(account.id) }
            }.onError {
                postEvent { Contract.Event.ShowToast(R.string.an_error_occurred) }
            }
    }.flowOn(appDispatchers.default)

    private fun handleFavoriteAccountIntent(account: Account): Flow<Reduce> {
        viewModelScope.launch(appDispatchers.io) {
            if (!account.isFavorite) {
                commonUseCases.addFavoriteAccountUseCase(account)
                    .onSuspendedSuccess { /*emit(Reduce.OnFavoriteAccount(true))*/ }
                    .onSuspendedError { postEvent(Contract.Event.ShowToast(R.string.favorite_error_add)) }
            } else {
                commonUseCases.removeFavoriteAccountUseCase(account)
                    .onSuspendedSuccess { /*emit(Reduce.OnFavoriteAccount(false))*/ }
                    .onSuspendedError { postEvent(Contract.Event.ShowToast(R.string.favorite_error_remove)) }
            }
        }//.flowOn(appDispatchers.io)
        return emptyFlow()
    }
}
