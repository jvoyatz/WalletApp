package gr.jvoyatz.assignment.wallet.accounts

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.testIn
import com.google.common.truth.Truth
import gr.jvoyatz.assignment.core.common.resultdata.ResultData
import gr.jvoyatz.assignment.core.common.resultdata.ResultData.Companion.success
import gr.jvoyatz.assignment.core.common.resultdata.asSuccess
import gr.jvoyatz.assignment.core.common.resultdata.toResultDataSuccess
import gr.jvoyatz.assignment.core.common.utils.mapList
import gr.jvoyatz.assignment.core.testing.MockData
import gr.jvoyatz.assignment.core.testing.fakes.FakeAppDispatchers
import gr.jvoyatz.assignment.core.testing.utils.MainDispatcherRule
import gr.jvoyatz.assignment.core.testing.utils.runWithTestDispatcher
import gr.jvoyatz.assignment.wallet.common.android.ui.mappers.toUiModel
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.models.AccountException
import gr.jvoyatz.assignment.wallet.domain.usecases.CommonUseCases
import gr.jvoyatz.assignment.wallet.domain.usecases.GetAccountsUseCase
import gr.jvoyatz.assignment.wallet.domain.usecases.UseCases
import io.mockk.Awaits
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.SpyK
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsViewModelTest{

    @get:Rule
    val rule = MainDispatcherRule()

    @SpyK
    private var savedStateHandle = SavedStateHandle()

    private val appDispatchers = FakeAppDispatchers()

    private val getAccountsUseCase: GetAccountsUseCase = mockk()
    private val setSelectedAccountUseCase: UseCases.SetSelectedAccountUseCase = mockk()

    private val addFavoriteAccountUseCase: UseCases.AddFavoriteAccountUseCase = mockk()
    private val removeFavoriteAccountUseCase: UseCases.RemoveFavoriteAccountUseCase = mockk( relaxed = true)
    private val commonUseCases: CommonUseCases = CommonUseCases (
        addFavoriteAccountUseCase = addFavoriteAccountUseCase,
        removeFavoriteAccountUseCase = removeFavoriteAccountUseCase
    )

    private lateinit var sut: AccountsViewModel

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        sut = AccountsViewModel(
            savedStateHandle,
            getAccountsUseCase,
            setSelectedAccountUseCase,
            appDispatchers,
            commonUseCases
        )
    }


    @Test
    fun `on initialize intent, then get accounts `() = runTest {
        //given
        val intent = Contract.Intent.Initialize

        //when
        sut.onNewIntent(intent)

        sut
        //then
        coVerify(exactly = 1) { getAccountsUseCase() }
    }

    @Test
    fun `on getData intent, then get accounts `() = runTest {
        //given
        val intent = Contract.Intent.GetData()

        //when
        sut.onNewIntent(intent)

        sut
        //then
        coVerify(exactly = 1) { getAccountsUseCase() }
    }


    @Test
    fun `on getData intent, then state is loading `() = runTest {
        //given
        val intent = Contract.Intent.GetData()
        coEvery { getAccountsUseCase() } returns flow{ listOf<Account>().toResultDataSuccess() }

        //when
        sut.uiState.test {
            sut.onNewIntent(intent)
            skipItems(1)
            val state = awaitItem().state


            Truth.assertThat(state).isEqualTo(Contract.ViewState.Loading)
        }
    }


    @Test
    fun `on getData intent returns with data, then state is Data  `() = runTest {
        //given
        val accounts = listOf(MockData.account)
        val intent = Contract.Intent.GetData()
        coEvery { getAccountsUseCase() } returns flow{ emit(accounts.toResultDataSuccess()) }

        //when
        sut.uiState.test {
            sut.onNewIntent(intent)
            skipItems(1)
            val state = awaitItem().state
            //then
            Truth.assertThat(state).isInstanceOf(Contract.ViewState.Data::class.java)
            val emissionData = (state as Contract.ViewState.Data).accounts
            Truth.assertThat(emissionData).isEqualTo(accounts.mapList { it.toUiModel() })
        }
    }

    @Test
    fun `on getData intent returns with empty data, then state is NoData  `() = runTest {
        //given
        val accounts = listOf<Account>()
        val intent = Contract.Intent.GetData()
        coEvery { getAccountsUseCase() } returns flow{ emit(accounts.toResultDataSuccess()) }

        //when
        sut.uiState.test {
            sut.onNewIntent(intent)
            skipItems(1)
            val state = awaitItem().state
            //then
            Truth.assertThat(state).isInstanceOf(Contract.ViewState.NoData::class.java)
        }
    }

    @Test
    fun `on getData intent returns error, then state is Error  `() = runTest {
        //given
        val intent = Contract.Intent.GetData()
        coEvery { getAccountsUseCase() } returns flow{ emit(ResultData.error(AccountException("network exception"))) }

        //when
        sut.uiState.test {
            sut.onNewIntent(intent)
            skipItems(1)
            val state = awaitItem().state
            //then
            Truth.assertThat(state).isInstanceOf(Contract.ViewState.Error::class.java)
        }
    }

    @Test
    fun `when an account has been marked as favorite, then save this account use case is called`() = runTest {
        //given
        val account = MockData.account.apply {
            isFavorite = false
        }
        //or relaxed mockk( relaxed = true)
        coEvery { addFavoriteAccountUseCase(account) } just Awaits

        sut.onNewIntent(Contract.Intent.OnFavoriteAccount(account))

        coVerify { addFavoriteAccountUseCase(account) }
    }


    val flow: MutableSharedFlow<ResultData<List<Account>>> = MutableSharedFlow(replay = 1)
    @SpyK
    var getAccountsUseCaseSpy =  object  : GetAccountsUseCase{

        override fun invoke(p1: Boolean): Flow<ResultData<List<Account>>> = flow
    }
    @Test
    fun `when an account has been marked as favorite, and save happened successfully, then refresh takes place`() = runTest {
        //given
        val accounts = listOf(MockData.account)
        val account = MockData.account.apply {
            isFavorite = false
        }
        coEvery { addFavoriteAccountUseCase(account) } coAnswers  {
            println("replicating the way flow with room library works")
            val updatedAccount = account.apply {
                this.isFavorite = true
            }
            flow.emit(success(listOf(updatedAccount)))
            success(Unit)
        }

        sut = AccountsViewModel(
            savedStateHandle,
            getAccountsUseCaseSpy,
            setSelectedAccountUseCase,
            appDispatchers,
            commonUseCases
        )

        val intent = Contract.Intent.GetData()
         coEvery { getAccountsUseCaseSpy() } returns flow{ emit(accounts.toResultDataSuccess()) }

        sut.uiState.test {
            sut.onNewIntent(intent)
            skipItems(1)
            sut.onNewIntent(Contract.Intent.OnFavoriteAccount(account))
        }

        val emissionTurbine = flow.testIn(this.backgroundScope)
        val item = emissionTurbine.awaitItem()
        //ensuring that emission contains a favorite account now
        Truth.assertThat(item.asSuccess()!!.data.first().isFavorite).isEqualTo(true)
    }

    @Test
    fun `when an account has been removed as favorite, then save this account use case is called`(){
        runWithTestDispatcher {
            //given
            val account = MockData.account.apply {
                isFavorite = true
            }
            coEvery { removeFavoriteAccountUseCase(account) } just Awaits

            sut.onNewIntent(Contract.Intent.OnFavoriteAccount(account))

            coVerify { removeFavoriteAccountUseCase(account) }
        }
    }

    @Test
    fun `when an account has been removed as favorite, and removal happened successfully, then refresh takes place`() = runTest {
        //given
        val accounts = listOf(MockData.account)
        val account = MockData.account.apply {
            isFavorite = true
        }
        coEvery { removeFavoriteAccountUseCase(account) } coAnswers  {
            println("replicating the way flow with room library works")
            val updatedAccount = account.apply {
                this.isFavorite = false
            }
            flow.emit(success(listOf(updatedAccount)))
            success(Unit)
        }

        sut = AccountsViewModel(
            savedStateHandle,
            getAccountsUseCaseSpy,
            setSelectedAccountUseCase,
            appDispatchers,
            commonUseCases
        )

        val intent = Contract.Intent.GetData()
        coEvery { getAccountsUseCaseSpy() } returns flow{ emit(accounts.toResultDataSuccess()) }

        sut.uiState.test {
            sut.onNewIntent(intent)
            skipItems(1)
            sut.onNewIntent(Contract.Intent.OnFavoriteAccount(account))
        }

        val emissionTurbine = flow.testIn(this.backgroundScope)
        val item = emissionTurbine.awaitItem()
        //ensuring that emission contains a favorite account now
        Truth.assertThat(item.asSuccess()!!.data.first().isFavorite).isEqualTo(false)
    }

    @Test
    fun `in case of an error when removing a favorite account, then show toast`(){
        runWithTestDispatcher {
            //given
            val account = MockData.account.apply {
                isFavorite = true
            }
            coEvery { removeFavoriteAccountUseCase(account) } returns ResultData.error(AccountException(""))

            sut.uiEvent.test {
                sut.onNewIntent(Contract.Intent.OnFavoriteAccount(account))

                val event = awaitItem()

                Truth.assertThat(event).isInstanceOf(Contract.Event.ShowToast::class.java)
            }
        }
    }

    @Test
    fun `in case of an error when adding a favorite account, then show toast`(){
        runWithTestDispatcher {
            //given
            val account = MockData.account.apply {
                isFavorite = false
            }
            coEvery { addFavoriteAccountUseCase(account) } returns ResultData.error(AccountException(""))

            sut.uiEvent.test {
                sut.onNewIntent(Contract.Intent.OnFavoriteAccount(account))

                val event = awaitItem()

                Truth.assertThat(event).isInstanceOf(Contract.Event.ShowToast::class.java)
            }
        }
    }

    @Test
    fun `in case of an error when clicking in an account, then emit AccountDetailsNavigation`(){
        runWithTestDispatcher {
            //given
            val account = MockData.account.apply {
                isFavorite = false
            }
            coEvery { setSelectedAccountUseCase(account) } returns ResultData.error(AccountException(""))

            sut.uiEvent.test {
                sut.onNewIntent(Contract.Intent.OnAccountSelected(account))

                val event = awaitItem()

                Truth.assertThat(event).isInstanceOf(Contract.Event.ShowToast::class.java)
            }
        }
    }

    @Test
    fun `in case of a click in an account, then emit AccountDetailsNavigation`(){
        runWithTestDispatcher {
            //given
            val account = MockData.account.apply {
                isFavorite = false
            }
            coEvery { setSelectedAccountUseCase(account) } returns ResultData.success(Unit)

            sut.uiEvent.test {
                sut.onNewIntent(Contract.Intent.OnAccountSelected(account))

                val event = awaitItem()

                Truth.assertThat(event).isInstanceOf(Contract.Event.AccountDetailsNavigation::class.java)
            }
        }
    }
}