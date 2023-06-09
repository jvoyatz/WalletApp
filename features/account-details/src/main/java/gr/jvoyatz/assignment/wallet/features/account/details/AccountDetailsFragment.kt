package gr.jvoyatz.assignment.wallet.features.account.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import gr.jvoyatz.assignment.core.common.utils.Constants
import gr.jvoyatz.assignment.core.common.utils.DateUtils
import gr.jvoyatz.assignment.core.ui.utils.hide
import gr.jvoyatz.assignment.core.ui.utils.show
import gr.jvoyatz.assignment.core.ui.utils.showToastShort
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountUiModel
import gr.jvoyatz.assignment.wallet.features.account.details.adapter.AccountTransactionsPagingAdapter
import gr.jvoyatz.assignment.wallet.features.account.details.databinding.FragmentAccountDetailsBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AccountDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAccountDetailsBinding
    private val viewModel: AccountDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
        setupRecyclerViews()
        setupObservers()
    }

    private fun setupRecyclerViews() {
        binding.transactionsList.apply {
            this.adapter = AccountTransactionsPagingAdapter()
        }
    }

    private fun setViewListeners() {
        with(binding) {
            accountContainer.setAccountFavoriteClickListener {
                viewModel.onNewIntent(Contract.Intent.FavoriteAdded)
            }

            val getDataClickHandler = {
                viewModel.onNewIntent(Contract.Intent.GetData(extractAccountId()))
            }

            loaderView.setRetryListener(getDataClickHandler)
          //  swipeContainer.isEnabled = false // disabled due to issue with click listeners
            swipeContainer.setOnRefreshListener(getDataClickHandler)
            transactionsLoader.setRetryListener(getDataClickHandler)


            nestedScrollview.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
                val safeNestedScrollView = v ?: return@setOnScrollChangeListener

                safeNestedScrollView.getChildAt(safeNestedScrollView.childCount - 1).let { last ->
                    val safeView = last ?: return@setOnScrollChangeListener

                    val adapter = binding.transactionsList.adapter as AccountTransactionsPagingAdapter
                    if (((scrollY >= (safeView.measuredHeight - safeNestedScrollView.measuredHeight)) && scrollY > oldScrollY) &&
                        (!adapter.isLoading() && viewModel.canLoadMoreTransactions(/*itemCount*/))
                    ) {
                        adapter.showLoading()
                        viewModel.onNewIntent(Contract.Intent.GetMoreTransactions())
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.uiState
            .map { it.viewState }
            .onEach {
                handleScreenState(it)
            }
            .launchIn(lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect {
                    handleEvent(it)
                }
            }
        }
    }

    private fun handleEvent(event: Contract.Event) {
        when (event) {
            is Contract.Event.ShowToast -> context.showToastShort(getString(event.resId))
        }
    }

    private fun handleScreenState(state: Contract.ViewState) {
        when (state) {
            Contract.ViewState.Initialize -> extractAccountId().let { viewModel.onNewIntent(Contract.Intent.GetData(it)) }
            Contract.ViewState.Loading -> showLoadingState()
            Contract.ViewState.Error -> showErrorState()
            is Contract.ViewState.Data -> showDataState(state.account)
        }
    }

    private fun showLoadingState(){
        with(binding){
            swipeContainer.isRefreshing = true
            nestedScrollview.hide()
            loaderView.showLoading()
        }
    }

    private fun showErrorState(){
        with(binding) {
            swipeContainer.isRefreshing = false
            nestedScrollview.hide()
            loaderView.showError()
        }
    }

    private fun showDataState(account: AccountUiModel) {
        with(binding) {
            swipeContainer.isRefreshing = false
            loaderView.hide()
            nestedScrollview.show()

            accountContainer.setAccountName(account.accountNickname)
            accountContainer.setAccountBalance(account.balance, account.currencyCode)
            accountContainer.setAccountFavorite(account.isFavorite)

            account.details?.let {
                this.accountDate.text = if(it.openedDate.isBlank()) Constants.DOTS else DateUtils.formatToDateStr(it.openedDate)
                this.accountType.text = account.accountType.type
                this.accountProduct.text = it.productName
                this.accountBranch.text = it.branch
                this.accountBeneficiaries.text = it.beneficiaries
            } ?: kotlin.run {
                binding.accountExtraContainer.visibility = View.INVISIBLE
            }

            if(account.transactions == null){
                binding.transactionsLoader.showError()
                binding.transactionsList.hide()
            }else if(account.transactions.isNullOrEmpty()){
                binding.transactionsLoader.showNoData()
                binding.transactionsList.hide()
            } else {
                binding.transactionsLoader.hide()
                with(binding.transactionsList) {
                    val adapter = this.adapter as AccountTransactionsPagingAdapter
                    adapter.submitList(account.transactions!!)
                }
                binding.transactionsList.show()
            }
        }
    }

    /**
     * Extracts the id passed through navigation component safe args
     * eg the id of the account selected by the user.
     *
     * we only pass the id as argument here in order not break the single source
     * of truth principle.
     */
    private fun extractAccountId(): String = arguments?.getString(ACCOUNT_ID_KEY)
        ?: throw IllegalStateException("could not continue without id")

    companion object {
        private const val ACCOUNT_ID_KEY = "accountId"
    }
}