package gr.jvoyatz.assignment.core.ui.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

/**
 * Not working ok with nestedscrollview, due to different ui measurements
 */
abstract class PagingScrollingListener(
    private val layoutManager: LinearLayoutManager,
    private val pageSize: Int
) :
    RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        Timber.d("onScrollStateChanged() called with: recyclerView = " + recyclerView + ", newState = " + newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        Timber.d("onScrolled() called with: recyclerView = " + recyclerView + ", dx = " + dx + ", dy = " + dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        Timber.d("visibleItemCount [$visibleItemCount], totalItemCount [$totalItemCount], firstVisibleItemPisition[$firstVisibleItemPosition]")


        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= pageSize
            ) {
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}