package gr.jvoyatz.assignment.wallet.features.account.details

import android.os.Parcelable
import androidx.annotation.StringRes
import gr.jvoyatz.assignment.core.mvvmi.ReducedState
import gr.jvoyatz.assignment.core.mvvmi.UiEvent
import gr.jvoyatz.assignment.core.mvvmi.UiIntent
import gr.jvoyatz.assignment.core.mvvmi.UiState
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountUiModel
import gr.jvoyatz.assignment.wallet.common.android.ui.models.TransactionUI
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.PagedTransactions
import gr.jvoyatz.assignment.wallet.domain.models.Transaction
import kotlinx.parcelize.Parcelize

object Contract {

    sealed interface Intent: UiIntent{
        data class GetData(val accountId: String): Intent
        data class GetMoreTransactions(
            val dateFrom: String ?= null,
            val dateTo: String ?= null
        ): Intent
        object FavoriteAdded: Intent
    }

    sealed interface Event: UiEvent{
        data class ShowToast(@StringRes val resId: Int): Event
      //  data class AddedAsFavorite(val isFavorite: Boolean): Event
    }

    @Parcelize
    data class State(
        val viewState: ViewState
    ):UiState

    sealed interface Reduced: ReducedState {
        data class Data(val account: Account): Reduced
        data class TransactionsNextPage(val pagedTransactions: PagedTransactions): Reduced
        data class OnFavoriteAccount(val isFavorite: Boolean): Reduced
        object Error: Reduced
    }

    @Parcelize
    sealed class ViewState : Parcelable{
        object Initialize : ViewState()
        object Loading: ViewState()
        object Error: ViewState()
        data class Data(val account: AccountUiModel): ViewState()
    }
}