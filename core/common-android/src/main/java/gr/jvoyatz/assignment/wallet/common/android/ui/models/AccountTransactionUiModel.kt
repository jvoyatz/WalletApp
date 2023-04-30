package gr.jvoyatz.assignment.wallet.common.android.ui.models

import android.os.Parcelable
import gr.jvoyatz.assignment.wallet.domain.models.Paging
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class TransactionUI: Parcelable{
    data class Date(val date: String): TransactionUI()
    data class Holder(val transactionUiModel: AccountTransactionUiModel): TransactionUI()
    object Loading: TransactionUI()
}

@Parcelize
data class PagingUiModel(
    val currentPage: Int = Paging.UNKNOWN,
    val pagesCount: Int= Paging.UNKNOWN,
    val totalItems: Int = Paging.UNKNOWN
): Parcelable {

    @IgnoredOnParcel
    var isLoading: Boolean = false
    val canLoadMore: Boolean
        get() = (pagesCount >= 0) && (currentPage + 1) < pagesCount
}

@Parcelize
data class AccountTransactionUiModel(
    val date: String,
    val description: String?,
    val id: String,
    val isDebit: Boolean,
    val transactionAmount: String,
    val transactionType: String,
) : Parcelable