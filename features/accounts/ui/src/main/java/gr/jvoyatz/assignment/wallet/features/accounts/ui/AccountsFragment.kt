package gr.jvoyatz.assignment.wallet.features.accounts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import gr.jvoyatz.assignment.wallet.common.android.navigation.Destination
import gr.jvoyatz.assignment.wallet.common.android.navigation.Navigator
import gr.jvoyatz.assignment.core.ui.utils.fromBottomAnimation
import gr.jvoyatz.assignment.core.ui.utils.hide
import gr.jvoyatz.assignment.wallet.common.android.domain.models.AccountType
import gr.jvoyatz.assignment.wallet.features.accounts.domain.models.Account
import gr.jvoyatz.assignment.wallet.features.accounts.ui.adapter.AccountListAdapter
import gr.jvoyatz.assignment.wallet.features.accounts.ui.databinding.FragmentAccountsListBinding
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class AccountsFragment : Fragment() {

    @Inject
    lateinit var navigator: gr.jvoyatz.assignment.wallet.common.android.navigation.Navigator;
    private lateinit var binding: FragmentAccountsListBinding
    private val viewModel: AccountsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return with(FragmentAccountsListBinding.inflate(inflater, container, false)){
            binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeLayout()
        setupLoaderView()
        setupRecyclerViews()
        setupObservers()
        showDataState(listOf())
    }

    private fun setupSwipeLayout(){
        with(binding.swipeLayout){
            this.setOnRefreshListener {
                Timber.d("get data")
            }
        }
    }

    private fun setupLoaderView() {
        with(binding.loaderView){
            this.showLoading()
            this.setRetryListener {
                Timber.d("get data")
            }
        }
    }

    private fun setupRecyclerViews(){
        val adapter = AccountListAdapter {
            navigator.navigate(gr.jvoyatz.assignment.wallet.common.android.navigation.Destination.AccountDetails(1))
        }

        binding.dataList.apply {
            setHasFixedSize(true)
            this.adapter = adapter
        }
    }

    private fun setupObservers(){

    }

    //ui state methods

    private fun showLoadingState(){
        with(binding.loaderView){
            showLoading()
        }
    }

    private fun showDataState(accounts: List<Account>){
        val adapter = binding.dataList.adapter as AccountListAdapter
        binding.loaderView.hide()
        adapter.submitList(listOf(Account("1324", "asdf", 1234, AccountType.CREDIT_CARD, "sdf", "sf")))
        binding.dataList.fromBottomAnimation()
    }

    private fun showNoDataState(){
        with(binding.loaderView){
            showNoData()
        }
    }

    private fun showErrorState(){
        with(binding.loaderView){
            showError()
        }
    }
}