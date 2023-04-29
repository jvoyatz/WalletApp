package gr.jvoyatz.assignment.wallet.features.account.details.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import gr.jvoyatz.assignment.wallet.features.account.details.databinding.FragmentAccountDetailsBinding
import timber.log.Timber
import java.lang.IllegalStateException


@AndroidEntryPoint
class AccountDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAccountDetailsBinding
    private val viewModel: AccountDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = extractAccountId()
        Timber.d("account id is $id")

        //binding.account.setAccountName("asdfasdfsad")
    }


    /**
     * Extracts the id passed through navigation component safe args
     * eg the id of the account selected by the user.
     *
     * we only pass the id as argument here in order not break the single source
     * of truth principle.
     */
    private fun extractAccountId(): Int = arguments?.getInt(ACCOUNT_ID_KEY)
        ?: throw IllegalStateException("could not continue without id")
    companion object{
        private const val ACCOUNT_ID_KEY = "accountId"
    }
}