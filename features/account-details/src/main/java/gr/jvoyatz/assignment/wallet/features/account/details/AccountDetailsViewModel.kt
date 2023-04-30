package gr.jvoyatz.assignment.wallet.features.account.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.core.common.resultdata.asSuccess
import gr.jvoyatz.assignment.core.common.resultdata.isSuccess
import gr.jvoyatz.assignment.core.common.resultdata.onError
import gr.jvoyatz.assignment.core.common.resultdata.onSuccess
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedError
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedSuccess
import gr.jvoyatz.assignment.core.mvvmi.BaseViewModel
import gr.jvoyatz.assignment.core.ui.R.string.unexpected_error
import gr.jvoyatz.assignment.wallet.common.android.AppDispatchers
import gr.jvoyatz.assignment.wallet.common.android.TimberDebugTree
import gr.jvoyatz.assignment.wallet.common.android.ui.mappers.toUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.mappers.toUiModels
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions
import gr.jvoyatz.assignment.wallet.domain.usecases.CommonUseCases
import gr.jvoyatz.assignment.wallet.domain.usecases.UseCases
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.account
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.accountId
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.canLoadMore
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.getPaging
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.isLoadingTransactions
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.updatePaging
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import gr.jvoyatz.assignment.core.ui.R.string.favorite_error_add as favorite_account_add_error_msg_id
import gr.jvoyatz.assignment.core.ui.R.string.favorite_error_remove as favorite_account_remove_error_msg_id
import gr.jvoyatz.assignment.core.ui.R.string.transactions_next_page_error as transactionsNextPageErrorMsgId

@HiltViewModel
class AccountDetailsViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appDispatchers: AppDispatchers,
    private val getAccountDetailsUseCase: UseCases.GetAccountDetailsUseCase,
    private val getAccountTransactionsUserCase: UseCases.GetAccountTransactionsUserCase,
    private val commonUseCases: CommonUseCases
) : BaseViewModel<Contract.State, Contract.Reduced, Contract.Intent, Contract.Event>(
    savedStateHandle,
    Contract.State(Contract.ViewState.Initialize)
) {
    fun isLoadingTransactions() = uiState.value.viewState.isLoadingTransactions

    override fun mapIntents(intent: Contract.Intent): Flow<Contract.Reduced> {
        return when (intent) {
            is Contract.Intent.GetData -> getData(intent.accountId)
            is Contract.Intent.GetMoreTransactions -> getTransactionsNextPage()
            is Contract.Intent.FavoriteAdded -> return handleFavoriteAccountIntent()
        }
    }

    override fun reduceUiState(
        uiState: Contract.State,
        reduceState: Contract.Reduced
    ): Contract.State {
        val state = when (reduceState) {
            is Contract.Reduced.Data -> Contract.ViewState.Data(reduceState.account.toUiModel())
            is Contract.Reduced.TransactionsNextPage -> getViewStateForTransactionNextPage(
                uiState.viewState,
                reduceState
            )
            is Contract.Reduced.Error -> Contract.ViewState.Error
            is Contract.Reduced.OnFavoriteAccount -> getViewStateForFavoriteAccount(uiState.viewState, reduceState)
        }
        return uiState.copy(viewState = state)
    }

    private fun getViewStateForFavoriteAccount(
        viewState: Contract.ViewState,
        reduceState: Contract.Reduced.OnFavoriteAccount
    ): Contract.ViewState{
        return with(viewState) {
            if (this is Contract.ViewState.Data) {
                val updatedAccount = account.run {
                    copy(isFavorite = reduceState.isFavorite)
                }
                Contract.ViewState.Data(updatedAccount)
            } else {
                throw IllegalStateException("eager crash to detect errors, this place must never be reached")
            }
        }
    }
    private fun getViewStateForTransactionNextPage(
        viewState: Contract.ViewState,
        reduceState: Contract.Reduced.TransactionsNextPage
    ): Contract.ViewState {
        return with(viewState) {
            if (this is Contract.ViewState.Data) {
                val pagedTransactions = reduceState.pagedTransactions
                updatePaging(pagedTransactions.paging)
                val updatedAccount = account.run {
                    copy(transactions = transactions!! + pagedTransactions.transactions.toUiModels())
                }
                Contract.ViewState.Data(updatedAccount)
            } else {
                throw IllegalStateException("eager crash to detect errors, this place must never be reached")
            }
        }
    }

    /**
     * Gets the data needed to init this screen
     */
    private fun getData(accountId: String): Flow<Contract.Reduced> = flow {
        val errorBlock: suspend () -> Unit = { emit(Contract.Reduced.Error) }
        if (accountId.isBlank()) {
            errorBlock()
            return@flow
        }

        //prepare
        val details = viewModelScope.async(appDispatchers.io) {
            getAccountDetailsUseCase(accountId)
        }
        val transactions = getTransactions(accountId)!!

        //get details
        with(details.await()) {
            if (isSuccess()) { // if result isSuccess
                this.asSuccess()!!.data.apply {
                    //get transactions
                    var trResult = transactions.await()
                    if (trResult.isSuccess()) {
                        this.pagedTransactions = trResult.asSuccess()!!.data
                        emit(Contract.Reduced.Data(this))
                    } else {
                        errorBlock()
                    }
                }
            } else {
                errorBlock()
            }
        }
    }.flowOn(appDispatchers.default)

    private fun getTransactions(
        accountId: String,
        dateFrom: String? = null,
        dateTo: String? = null
    ): Deferred<ResultData<PagedTransactions>> {
        return uiState.value.viewState.let {
            val page = it.getPaging()?.let { pagingModel ->
                pagingModel.isLoading = true
                pagingModel.currentPage + 1
            }
            viewModelScope.async(appDispatchers.io) {
                getAccountTransactionsUserCase(accountId, page ?: 0, dateFrom, dateTo)
            }
        }
    }

    private fun getTransactionsNextPage() = flow {
        getTransactions(uiState.value.viewState.accountId!!).await()
            .onSuspendedSuccess { emit(Contract.Reduced.TransactionsNextPage(it)) }
            .onSuspendedError { postEvent(Contract.Event.ShowToast(transactionsNextPageErrorMsgId)) }
    }

    private fun handleFavoriteAccountIntent(): Flow<Contract.Reduced> = flow {
        //viewModelScope.launch(appDispatchers.io){
            val account = uiState.value.viewState.account ?: kotlin.run {
                postEvent(Contract.Event.ShowToast(unexpected_error))
                return@flow
            }

            Timber.d("isfavorite ${account.isFavorite}")
            if (!account.isFavorite) {
                commonUseCases.addFavoriteAccountUseCase(account)
                    .onSuspendedSuccess {
                        Timber.d("set as favorite success!!!!")
                        emit(Contract.Reduced.OnFavoriteAccount(true))
                    }
                    .onSuspendedError {
                        Timber.e(it)
                        postEvent(Contract.Event.ShowToast(favorite_account_add_error_msg_id))
                    }
            } else {
                commonUseCases.removeFavoriteAccountUseCase(account)
                    .onSuspendedSuccess {
                        emit(Contract.Reduced.OnFavoriteAccount(false))
                    }
                    .onSuspendedError {
                        postEvent(Contract.Event.ShowToast(favorite_account_remove_error_msg_id))
                    }
            }
       // }
    }.flowOn(appDispatchers.io)

    /**
     * check whether we are able to load more transactions
     */
    fun canLoadMoreTransactions(): Boolean = with(uiState.value) {
        return if (viewState is Contract.ViewState.Data) viewState.canLoadMore else false
    }
}