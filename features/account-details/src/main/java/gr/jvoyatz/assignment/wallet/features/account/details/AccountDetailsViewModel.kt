package gr.jvoyatz.assignment.wallet.features.account.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.core.common.resultdata.asSuccess
import gr.jvoyatz.assignment.core.common.resultdata.isSuccess
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedError
import gr.jvoyatz.assignment.core.common.resultdata.onSuspendedSuccess
import gr.jvoyatz.assignment.core.mvvmi.BaseViewModel
import gr.jvoyatz.assignment.core.ui.R.string.unexpected_error
import gr.jvoyatz.assignment.wallet.common.android.AppDispatchers
import gr.jvoyatz.assignment.wallet.common.android.ui.mappers.toUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.mappers.toUiModels
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions
import gr.jvoyatz.assignment.wallet.domain.usecases.CommonUseCases
import gr.jvoyatz.assignment.wallet.domain.usecases.UseCases
import gr.jvoyatz.assignment.wallet.features.account.details.Contract.Reduce
import gr.jvoyatz.assignment.wallet.features.account.details.Contract.ViewState
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.account
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.accountId
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.canLoadMore
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.getPaging
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.isLoadingTransactions
import gr.jvoyatz.assignment.wallet.features.account.details.ContractExtensions.updatePaging
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
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
) : BaseViewModel<Contract.State, Reduce, Contract.Intent, Contract.Event>(
    savedStateHandle,
    Contract.State(ViewState.Initialize)
){
    @Deprecated("used a more appropriate way to determine whether recyclerview is loading more items")
    fun isLoadingTransactions() = uiState.value.viewState.isLoadingTransactions

    override fun mapIntents(intent: Contract.Intent): Flow<Reduce> {
        return when (intent) {
            is Contract.Intent.GetData -> getData(intent.accountId)
            is Contract.Intent.GetMoreTransactions -> getTransactionsNextPage()
            is Contract.Intent.FavoriteAdded -> return handleFavoriteAccountIntent()
            is Contract.Intent.UnexpectedError -> flowOf(Reduce.Error)
        }
    }

    override fun reduceUiState(state: Contract.State, reduce: Reduce): Contract.State {
        val newState = when (reduce) {
            is Reduce.Data -> ViewState.Data(reduce.account.toUiModel())
            is Reduce.TransactionsNextPage -> getTransactionPagingState(state.viewState, reduce)
            is Reduce.Error -> ViewState.Error
            is Reduce.OnFavoriteAccount -> getFavoriteAccountState(state.viewState, reduce)
            is Reduce.Loading -> ViewState.Loading
        }
        return state.copy(viewState = newState)
    }


    private fun getFavoriteAccountState(viewState: ViewState, reduceState: Reduce.OnFavoriteAccount): ViewState{
        return with(viewState) {
            if (this is ViewState.Data) {
                val updatedAccount = account.run {
                    copy(isFavorite = reduceState.isFavorite)
                }
                ViewState.Data(updatedAccount)
            }else{
                ViewState.Error
            }
        }
    }
    private fun getTransactionPagingState(viewState: ViewState, reduceState: Reduce.TransactionsNextPage): ViewState {
        return with(viewState) {
            if (this is ViewState.Data) {
                val pagedTransactions = reduceState.pagedTransactions
                updatePaging(pagedTransactions.paging)
                val updatedAccount = account.run {
                    copy(transactions = transactions!! + pagedTransactions.transactions.toUiModels())
                }
                ViewState.Data(updatedAccount)
            } else{
                ViewState.Error
            }
        }
    }

    /**
     * Gets the data needed to init this screen
     */
    private fun getData(accountId: String): Flow<Reduce> = flow {
        emit(Reduce.Loading)
        val errorBlock: suspend () -> Unit = { emit(Reduce.Error) }
        if (accountId.isBlank()) {
            errorBlock()
            return@flow
        }

        //prepare
        val details = viewModelScope.async(appDispatchers.io) {
            getAccountDetailsUseCase(accountId)
        }

        //get details
        val result = details.await()
        if (result.isSuccess()) {
            emit(Reduce.Data(result.asSuccess()!!.data))
        } else {
            errorBlock()
        }
    }.flowOn(appDispatchers.default)


    @Deprecated("do not use/left here to explain why this piece of code is wrong")
    private fun _getData(accountId: String): Flow<Reduce> = flow {
        emit(Reduce.Loading)
        val errorBlock: suspend () -> Unit = { emit(Reduce.Error) }
        if (accountId.isBlank()) {
            errorBlock()
            return@flow
        }

        //prepare
        val details = viewModelScope.async(appDispatchers.io) {
            getAccountDetailsUseCase(accountId)
        }
        val transactions = getTransactions(accountId)

        //get details
        with(details.await()) {
            if (isSuccess()) { // if result isSuccess
                this.asSuccess()!!.data.apply {

                    val trResult = transactions.await() //get transactions
                    if (trResult.isSuccess()) {
                        this.pagedTransactions = trResult.asSuccess()!!.data
                        emit(Reduce.Data(this))
                    } else {
                        errorBlock()
                    }
                }
            } else {
                errorBlock()
            }
        }
    }.flowOn(appDispatchers.default)

    private fun getTransactions(accountId: String, dateFrom: String? = null, dateTo: String? = null
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
        kotlinx.coroutines.delay(350)
        getTransactions(uiState.value.viewState.accountId!!).await()
            .onSuspendedSuccess { emit(Reduce.TransactionsNextPage(it)) }
            .onSuspendedError { postEvent(Contract.Event.ShowToast(transactionsNextPageErrorMsgId)) }
    }

    private fun handleFavoriteAccountIntent(): Flow<Reduce> = flow {
            val account = uiState.value.viewState.account ?: kotlin.run {
                postEvent(Contract.Event.ShowToast(unexpected_error))
                return@flow
            }

            if (!account.isFavorite) {
                commonUseCases.addFavoriteAccountUseCase(account)
                    .onSuspendedSuccess { emit(Reduce.OnFavoriteAccount(true)) }
                    .onSuspendedError { postEvent(Contract.Event.ShowToast(favorite_account_add_error_msg_id)) }
            } else {
                commonUseCases.removeFavoriteAccountUseCase(account)
                    .onSuspendedSuccess { emit(Reduce.OnFavoriteAccount(false)) }
                    .onSuspendedError { postEvent(Contract.Event.ShowToast(favorite_account_remove_error_msg_id)) }
            }
    }.flowOn(appDispatchers.io)

    /**
     * check whether we are able to load more transactions
     */
    fun canLoadMoreTransactions(): Boolean = with(uiState.value) {
        return if (viewState is ViewState.Data) viewState.canLoadMore else false
    }
}