package gr.jvoyatz.assignment.core.mvvmi

import android.os.Parcelable

/**
 * Represents the state of the views or in other words,
 * what our views must display
 */
interface UiState : Parcelable

/**
 * Given a user action (or intent -- aka [UiIntent], we execute a certain use case (through IntentFlow)
 * Then after we have taken the result of this action, we wrap its result
 * into an instance of this type.
 * Later on, we process this InternalPartialState to generate the final UiState,
 * that will be rendered in our screen
 */
interface InternalPartialState