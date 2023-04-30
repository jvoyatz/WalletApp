package gr.jvoyatz.assignment.wallet.features.account.details

import gr.jvoyatz.assignment.wallet.common.android.ui.mappers.toDomain
import gr.jvoyatz.assignment.wallet.common.android.ui.models.PagingUiModel
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.Paging

object ContractExtensions {

    internal val Contract.ViewState.account: Account?
        get() = if (this is Contract.ViewState.Data) this.account.toDomain() else null
    internal val Contract.ViewState.accountId: String?
        get() = if (this is Contract.ViewState.Data) this.account.id else null

    internal val Contract.ViewState.Data.canLoadMore: Boolean
        get() = account.paging?.let { it.canLoadMore } ?: false

    internal val Contract.ViewState.Data.totalItems: Int
        get() = account.paging?.let { it.totalItems } ?: 0

    internal fun Contract.ViewState.getPaging(): PagingUiModel? =
        if (this is Contract.ViewState.Data) this.account.paging else null

    internal fun Contract.ViewState.updatePaging(newPaging: Paging) {
        if (this is Contract.ViewState.Data && this.account.paging != null) {
            account.paging = account.paging!!.copy(
                currentPage = newPaging.currentPage,
                totalItems = newPaging.totalItems,
                pagesCount = newPaging.pagesCount
            )
        }
    }

    /**
     * check if a transactions loading is currently in progress
     */
    internal val Contract.ViewState.isLoadingTransactions : Boolean
        get() {
            if (this is Contract.ViewState.Data && this.account.paging != null) {
                return account.paging!!.isLoading
            }
            return false
        }
}