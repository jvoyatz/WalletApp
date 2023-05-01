//commented out due to building issues

//package gr.jvoyatz.assignment.wallet.accounts
//
//
//import androidx.lifecycle.SavedStateHandle
//import app.cash.turbine.test
//import com.google.common.truth.Truth
//import gr.jvoyatz.assignment.core.common.resultdata.ResultData
//import gr.jvoyatz.assignment.core.testing.fakes.FakeAppDispatchers
//import gr.jvoyatz.assignment.core.testing.utils.MainDispatcherRule
//import gr.jvoyatz.assignment.core.testing.utils.TestUtils
//import gr.jvoyatz.assignment.wallet.accounts.fakes.FakeApiSource
//import gr.jvoyatz.assignment.wallet.accounts.fakes.FakeDbSource
//import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
//import gr.jvoyatz.assignment.wallet.data.accounts.AccountRepositoryImpl
//import gr.jvoyatz.assignment.wallet.domain.models.Account
//import gr.jvoyatz.assignment.wallet.domain.usecases.CommonUseCases
//import gr.jvoyatz.assignment.wallet.domain.usecases.GetAccountsUseCase
//import gr.jvoyatz.assignment.wallet.domain.usecases.UseCases
//import io.mockk.MockKAnnotations
//import io.mockk.impl.annotations.SpyK
//import io.mockk.mockk
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.BeforeClass
//import org.junit.Rule
//import org.junit.Test
//
///**
// * You might see duplicate classes (eg FakeApiSource & FakeDbSource) or some other duplicate code here and there,
// * this was an issue of minor importance in front of the importance of this test.
// *
// * This test is an integration test, where we test two or more
// * (in our case we test viewmodel -> usecase -> repository (and then we use a fake impl as a datasource for account data).
// *
// * When applying unit test practices on a certain class, then tests should run on isolation.
// * Having that means, that we don't need to care about how the dependencies of those classes behave, we only
// * focus on the input/output of the method/class --> subject under test.
// *
// * However, this is not true when we want to test many components together.
// *
// * To start with, I create an AccountRepository with two fake sources.
// * Then this repository is used to create the use cases, which are finally passed
// * as argument in our viewmodel.
// *
// * Then, during testing viewmodel tries to `fetch` data using all these objects created for this test.
// */
//@OptIn(ExperimentalCoroutinesApi::class)
//class AccountsViewModelIntTest {
//
//    @get:Rule
//    val rule = MainDispatcherRule()
//
//    private var fakeApiSource: FakeApiSource = FakeApiSource()
//    private var fakeDbSource: FakeDbSource = FakeDbSource()
//    private  var repository = AccountRepositoryImpl(fakeApiSource, fakeDbSource)
//
//    @SpyK
//    private var savedStateHandle = SavedStateHandle()
//    private val appDispatchers = FakeAppDispatchers()
//
//    @SpyK
//    private  var getAccountsUseCase: GetAccountsUseCase =  object : GetAccountsUseCase {
//        override fun invoke(refresh: Boolean): Flow<ResultData<List<Account>>> =
//            repository.getAccounts()
//    }
//
//    private val setSelectedAccountUseCase: UseCases.SetSelectedAccountUseCase = mockk()
//    private val addFavoriteAccountUseCase: UseCases.AddFavoriteAccountUseCase = mockk()
//    private val removeFavoriteAccountUseCase: UseCases.RemoveFavoriteAccountUseCase =
//        mockk(relaxed = true)
//    private val commonUseCases: CommonUseCases = CommonUseCases(
//        addFavoriteAccountUseCase = addFavoriteAccountUseCase,
//        removeFavoriteAccountUseCase = removeFavoriteAccountUseCase
//    )
//
//    private lateinit var sut: AccountsViewModel
//
//    @Before
//    fun setup() {
//        MockKAnnotations.init(this)
//
//        val test: FakeApiSource
//        sut = AccountsViewModel(
//            savedStateHandle,
//            getAccountsUseCase,
//            setSelectedAccountUseCase,
//            appDispatchers,
//            commonUseCases
//        )
//    }
//
//
//    @Test
//    fun `on getData intent returns with data, then state is Data (from the remote service) `() = runTest {
//        //given
//        val intent = Contract.Intent.GetData()
//
//        //when
//        sut.uiState.test {
//            sut.onNewIntent(intent)
//            skipItems(1)
//
//            fakeApiSource.setSuccessApiResponse(dto)
//            fakeDbSource.emit(listOf())
//
//            val state = awaitItem().state
//
//            //then
//            Truth.assertThat(state).isInstanceOf(Contract.ViewState.Data::class.java)
//            val emissionData = (state as Contract.ViewState.Data).accounts
//            Truth.assertThat(emissionData.size).isEqualTo(dto.size)
//        }
//    }
//
//    companion object {
//        lateinit var dto: List<AccountRaw>
//        @JvmStatic
//        @BeforeClass
//        fun loadData() {
//            dto = TestUtils.getData<AccountRaw, AccountRaw>("/accounts.json") { this }
//        }
//    }
//}