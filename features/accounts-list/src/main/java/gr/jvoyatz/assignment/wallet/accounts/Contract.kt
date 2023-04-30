package gr.jvoyatz.assignment.wallet.accounts

import android.os.Parcelable
import androidx.annotation.StringRes
import gr.jvoyatz.assignment.core.mvvmi.ReducedState
import gr.jvoyatz.assignment.core.mvvmi.UiEvent
import gr.jvoyatz.assignment.core.mvvmi.UiIntent
import gr.jvoyatz.assignment.core.mvvmi.UiState
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountUiModel
import gr.jvoyatz.assignment.wallet.domain.models.Account
import kotlinx.parcelize.Parcelize

object Contract {


    /**
     * State is the type exposed by viewmodel and observed by
     * the View, which holds the a sealed class type representing
     * what the UI must render
     */
    @Parcelize
    data class State(val state: ViewState): UiState
    @Parcelize
    sealed class ViewState : Parcelable {
        object Initialize : ViewState()
        object Loading : ViewState()
        object Error : ViewState()
        object NoData : ViewState()
        data class Data(val accounts: List<AccountUiModel>) : ViewState()
    }

    /**
     * Partial or Reduce is used internally by the viewmodel in order to encapsulate the
     * result of the intent executed which be later reduced to
     * the final ScreenState holded inside State data class
     */
    sealed interface Reduce: ReducedState {
        data class Data(val data: List<Account>? = null): Reduce
        data class OnFavoriteAccount(val isFavorite: Boolean): Reduce
        object Error: Reduce
        object Loading: Reduce
    }


    /**
     * Used to indicate whether a toast should be displayed
     */
    sealed interface Event: UiEvent{
        data class ShowToast(@StringRes val resourceId: Int) : Event
        data class AccountDetailsNavigation(val id: String): Event
    }

    /**
     * Used to point what our viewmodel should execute
     */
    sealed interface Intent: UiIntent{
        object Initialize: Intent
        object GetData: Intent
        data class OnAccountSelected(val account: Account):Intent
    }
}