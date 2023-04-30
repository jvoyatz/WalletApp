package gr.jvoyatz.assignment.wallet.common.android.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AccountDetailsUiModel(
    val beneficiaries: String,
    val branch: String,
    val openedDate: String,
    val productName: String
) : Parcelable