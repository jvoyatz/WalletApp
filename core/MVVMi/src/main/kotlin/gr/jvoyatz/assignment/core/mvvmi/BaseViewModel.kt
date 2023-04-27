package gr.jvoyatz.assignment.core.mvvmi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Using this Base class we tell our View to draw something
 * related to the UiState field's value.
 *
 * This UiState is the result of the processing that takes place when a new
 * user intent is passed.
 *
 * In case of an error (eg a network error) then we show a UiSideEffect instance
 *
 * For each case of those described above, we use a different technology to achieve our purpose.
 * For the UiState we use StateFlow, something similar to Livedata but with a Initial Value.
 *
 * For the UiIntent we use MutableSharedFlow, which in case os an absence of a subscriber,
 * the value posted is being dropped. Why handling an Intent when no subscribers exist?
 *
 * For the UiEffect, I use Channels because Channel's value is being posted immediately and once.
 * However as described in this article, this is an anti-pattern and it is preferrable to use UiState to handle such cases.
 * @see [this](https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95)
 *
 * For now i will use Channels.
 */

private const val SAVED_UI_STATE_KEY = "SAVED_UI_STATE_KEY"
abstract class BaseViewModel<State: UiState, PartialState: PartialUiState, Intent: UiIntent, Event: UiEvent>(
    private val savedStateHandle: SavedStateHandle,
    initialState: State
) : ViewModel() {

    private val intentFlow : MutableSharedFlow<Intent> = MutableSharedFlow()

    val uiState = savedStateHandle.getStateFlow(SAVED_UI_STATE_KEY, initialState)

    private val _uiEventChannel = Channel<Event>(Channel.BUFFERED)
    val uiEvent = _uiEventChannel.receiveAsFlow()

    init {
        subscribeIntents()
    }

    /**
     * When a new emissions takes place in the intentFlow,
     * the `flatMapMerge` function is called and transforms the emitted Intent into
     * another flow.
     * This flow contains a PartialUiState value.
     * After the scan method is called, we pass this flow's value (aka PartialUiState) and the current value of the uiState's flow
     * to a function that returns (or reduces) the PartialUiState into UiState.
     *
     * In the end we set this value to the uiStateFlow.
     *
     * Note: Keep in mind that we use saveStateHandle to survive app's process death events.
     */
    private fun subscribeIntents(){
        viewModelScope.launch {
            intentFlow
                .flatMapMerge { mapIntents(it) }
                .scan(uiState.value, ::reduceUiState)
                .catch { Timber.e(it) }
                .collect {
                    savedStateHandle[SAVED_UI_STATE_KEY] = it
                }

        }
    }

    /**
     * Accepts an argument, which is an instance of [Intent], representing an action requested by the user.
     * This will trigger a new emission in the [intentFlow].
     */
    fun onNewIntent(intent: Intent){
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }

    /**
     * Posts a new event when we want to show a dialog to the user
     */
    protected fun postEvent(event: Event){
        viewModelScope.launch {
            _uiEventChannel.send(event)
        }
    }

    protected fun postEvent(builder: () -> Event){
        postEvent(builder())
    }
    /**
     * Handles user's intent's and after processing them, creates a new Partial Ui State, that will
     * be processed in order to update the UiState that View listens to
     */
    protected abstract fun mapIntents(intent: Intent): Flow<PartialState>

    /**
     * It updates the uiState in order to contain the new data included in the (new) Partial Ui State
     */
    protected abstract fun reduceUiState(
        currentUiState: State,
        partialUiState: PartialState
    ): State
}