package gr.jvoyatz.assignment.wallet.features.accounts.ui

import android.os.Parcelable
import androidx.annotation.StringRes
import gr.jvoyatz.assignment.core.mvvmi.InternalPartialState
import gr.jvoyatz.assignment.core.mvvmi.UiEvent
import gr.jvoyatz.assignment.core.mvvmi.UiIntent
import gr.jvoyatz.assignment.core.mvvmi.UiState
import gr.jvoyatz.assignment.wallet.features.accounts.domain.models.Account
import gr.jvoyatz.assignment.wallet.features.accounts.ui.model.AccountUi
import kotlinx.parcelize.Parcelize

object AccountsContract {


    /**
     * State is the type exposed by viewmodel and observed by
     * the View, which holds the a sealed class type representing
     * what the UI must render
     */
    @Parcelize
    data class State(val state: ScreenState): UiState
    @Parcelize
    sealed class ScreenState : Parcelable {
        object Initialize : ScreenState()
        object Loading : ScreenState()
        object Error : ScreenState()
        object NoData : ScreenState()
        data class Data(val accounts: List<AccountUi>) : ScreenState()

        /**
         * Partial is used internally by the viewmodel in order to encapsulate the
         * result of the intent executed which be later reduced to
         * the final ScreenState holded inside State data class
         */
        data class Partial(
            val isLoading: Boolean = false,
            val isError: Boolean = false,
            val isSuccess : Boolean = false,
            val data: List<Account>? = null
        ) : InternalPartialState
    }


    /**
     * Used to indicate whether a toast should be displayed
     */
    sealed interface Event: UiEvent{
        data class ShowToast(@StringRes val resourceId: Int) : Event
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