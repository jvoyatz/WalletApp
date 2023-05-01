package gr.jvoyatz.assignment.wallet.data.accounts

import app.cash.turbine.test
import com.google.common.truth.Truth
import gr.jvoyatz.assignment.core.common.resultdata.asError
import gr.jvoyatz.assignment.core.common.resultdata.asSuccess
import gr.jvoyatz.assignment.core.common.resultdata.isError
import gr.jvoyatz.assignment.core.common.resultdata.isSuccess
import gr.jvoyatz.assignment.core.common.utils.mapList
import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import gr.jvoyatz.assignment.core.testing.utils.TestUtils
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.toAccountEntity
import gr.jvoyatz.assignment.wallet.data.accounts.AccountMappers.toDomain
import gr.jvoyatz.assignment.wallet.data.accounts.fakes.FakeDbSource
import gr.jvoyatz.assignment.wallet.domain.models.Account
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository
import kotlinx.coroutines.test.runTest
import org.junit.BeforeClass
import org.junit.Test

class AccountRepositoryImplTest{

    private var fakeApiSource: FakeApiSource = FakeApiSource()
    private var fakeDbSource: FakeDbSource = FakeDbSource()
    private var sut: AccountsRepository = AccountRepositoryImpl(fakeApiSource, fakeDbSource)

    @Test
    fun `get accounts - first time & no favorites`() = runTest {
        //given
        fakeApiSource.setSuccessApiResponse(dto)
        fakeDbSource.emit(listOf())

        //when
        sut.getAccounts().test {
            val result = this.awaitItem()
            //then
            Truth.assertThat(result.isSuccess()).isTrue()
            Truth.assertThat(result.asSuccess()!!.data).isNotEmpty()
            Truth.assertThat(result.asSuccess()!!.data.filter { it.isFavorite }).isEmpty()
        }
    }


    @Test
    fun `get accounts - first time & 1 favorite`() = runTest {
        //given
        fakeApiSource.setSuccessApiResponse(dto)
        fakeDbSource.emit(listOf(entities.first()))

        //when
        sut.getAccounts().test {
            val result = this.awaitItem()
            //then
            Truth.assertThat(result.isSuccess()).isTrue()
            Truth.assertThat(result.asSuccess()!!.data).isNotEmpty()
            Truth.assertThat(result.asSuccess()!!.data.filter { it.isFavorite }).hasSize(1)
        }
    }


    @Test
    fun `get accounts - http exception`() = runTest {
        //given
        fakeApiSource.setHttpErrorApiResponse()
        fakeDbSource.emit(listOf(entities.first()))

        //when
        sut.getAccounts().test {
            val result = this.awaitItem()
            awaitComplete()

            //then
            Truth.assertThat(result.isError()).isTrue()
            Truth.assertThat(result.asError()).isNotNull()
            Truth.assertThat(result.asError()!!.exception).isNotNull()
        }
    }

    @Test
    fun `get accounts - io exception`() = runTest {
        //given
        fakeApiSource.setIoErrorApiResponse()
        fakeDbSource.emit(listOf(entities.first()))

        //when
        sut.getAccounts().test {
            val result = this.awaitItem()
            awaitComplete()

            //then
            Truth.assertThat(result.isError()).isTrue()
            Truth.assertThat(result.asError()).isNotNull()
            Truth.assertThat(result.asError()!!.exception).isNotNull()
        }
    }

    companion object {

        private lateinit var domain: List<Account>
        lateinit var entities: List<AccountEntity>
        lateinit var dto: List<AccountRaw>

        @JvmStatic
        @BeforeClass
        fun loadData() {
            dto = TestUtils.getData<AccountRaw, AccountRaw>("/accounts.json") { this }
            domain = dto.mapList { it.toDomain() }
            entities = domain.mapList { it.toAccountEntity() }
        }
    }
}