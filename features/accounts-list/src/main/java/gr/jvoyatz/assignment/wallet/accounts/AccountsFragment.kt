package gr.jvoyatz.assignment.wallet.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import gr.jvoyatz.assignment.core.ui.utils.hide
import gr.jvoyatz.assignment.wallet.accounts.adapter.AccountListAdapter
import gr.jvoyatz.assignment.wallet.accounts.databinding.FragmentAccountsListBinding
import gr.jvoyatz.assignment.wallet.common.android.navigation.Destination
import gr.jvoyatz.assignment.wallet.common.android.navigation.Navigator
import gr.jvoyatz.assignment.wallet.common.android.ui.mappers.toDomain
import gr.jvoyatz.assignment.wallet.common.android.ui.models.AccountUiModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AccountsFragment : Fragment() {

    @Inject
    lateinit var navigator: Navigator
    private lateinit var binding: FragmentAccountsListBinding
    private val viewModel: AccountsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return with(FragmentAccountsListBinding.inflate(inflater, container, false)){
            binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupSwipeLayout()
        setupLoaderView()
        setupObservers()
    }

    private fun setupSwipeLayout(){
        with(binding.swipeLayout){
            this.setOnRefreshListener {
                viewModel.onNewIntent(Contract.Intent.GetData(true))
            }
        }
    }

    private fun setupLoaderView() {
        with(binding.loaderView){
            this.setRetryListener {
                viewModel.onNewIntent(Contract.Intent.GetData(true))
            }
        }
    }

    private fun setupRecyclerViews(){
        val adapter = AccountListAdapter({
            viewModel.onNewIntent(Contract.Intent.OnAccountSelected(it.toDomain()))
        }) {
            viewModel.onNewIntent(Contract.Intent.OnFavoriteAccount(it.toDomain()))
        }

        binding.dataList.apply {
            setHasFixedSize(true)
            this.adapter = adapter
        }
    }

    private fun setupObservers(){
        viewModel.uiState
            .map { it.state }
            .onEach {
                handleScreenState(it)
            }
            .launchIn(lifecycleScope)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect {
                    when(it){
                        is Contract.Event.ShowToast -> {
                            val safeContext = context ?: return@collect
                            Toast.makeText(safeContext, it.resourceId, Toast.LENGTH_SHORT).show()
                        }
                        is Contract.Event.AccountDetailsNavigation -> {
                            navigator.navigate(Destination.AccountDetailsScreen(it.id))
                        }
                    }
                }
            }
        }
    }

    private fun handleScreenState(state: Contract.ViewState){
        when(state){
            Contract.ViewState.Initialize -> {
                viewModel.onNewIntent(Contract.Intent.GetData(false))
            }
            Contract.ViewState.Loading -> {
                showLoadingState()
            }
            Contract.ViewState.NoData -> {
                showNoDataState()
            }
            is Contract.ViewState.Data -> {
                showDataState(state.accounts)
            }
            Contract.ViewState.Error -> {
                showErrorState()
            }
        }
    }

    //ui state methods
    private fun showLoadingState(){
        with(binding){
            this.loaderView.showLoading()
            this.swipeLayout.isRefreshing = true
            this.dataList.isVisible = false
        }
    }

    private fun showDataState(accounts: List<AccountUiModel>){
        with(binding){
            val adapter = binding.dataList.adapter as AccountListAdapter
            this.loaderView.hide()
            adapter.submitList(accounts)
            this.swipeLayout.isRefreshing = false
            this.dataList.isVisible = true
        }
    }

    private fun showNoDataState(){
        with(binding){
            this.loaderView.showNoData()
            this.swipeLayout.isRefreshing = false
            this.dataList.isVisible = false
        }
    }

    private fun showErrorState(){
        with(binding){

            loaderView.showError()
            this.swipeLayout.isRefreshing = false
            this.dataList.isVisible = false
        }
    }
}