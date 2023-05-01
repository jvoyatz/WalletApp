@file:OptIn(ExperimentalCoroutinesApi::class)

package gr.jvoyatz.assignment.wallet.data.accounts

import com.google.common.truth.Truth
import gr.jvoyatz.assignment.core.testing.utils.TestUtils
import gr.jvoyatz.assignment.core.testing.utils.TestUtils.loadResourceFile
import gr.jvoyatz.assignment.wallet.core.api.WalletApi
import gr.jvoyatz.assignment.wallet.core.api.config.response.ApiResponse
import gr.jvoyatz.assignment.wallet.core.api.config.response.asHttpError
import gr.jvoyatz.assignment.wallet.core.api.config.response.asSuccess
import gr.jvoyatz.assignment.wallet.core.api.config.response.isError
import gr.jvoyatz.assignment.wallet.core.api.config.response.isSuccess
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.assignment.wallet.data.accounts.internal.ApiSourceImpl
import gr.jvoyatz.sportspot.core.testing.utils.ApiServer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.BeforeClass
import org.junit.Test

@ExperimentalCoroutinesApi
class ApiSourceTest : ApiServer<WalletApi>() {

    private lateinit var apiSource: ApiSource

    init {
        clazz = WalletApi::class.java
    }

    override fun setupServer() {
        super.setupServer()
        apiSource = ApiSourceImpl(apiClient)
    }

    @Test
    fun `get data - 200 OK `() = runTest {
        //given
        enqueue {
            success(rawAccounts!!)
        }

        //when
        val response = apiSource.getAccounts()


        //then
        Truth.assertThat(response).isNotNull()
        Truth.assertThat(response.isSuccess()).isTrue()
        Truth.assertThat(response.asSuccess()!!.body).isNotEmpty()
    }

    @Test
    fun `get data - 400 httpError `() = runTest {
        //given
        enqueue {
            httpError()
        }

        //when
        val apiResponse = apiSource.getAccounts()

        Truth.assertThat(apiResponse).isNotNull()
        Truth.assertThat(apiResponse.isError()).isTrue()
        Truth.assertThat(apiResponse).isInstanceOf(ApiResponse.HttpError::class.java)
        val httperror = apiResponse.asHttpError()
        Truth.assertThat(httperror!!.code).isEqualTo(400)
    }


    @Test
    fun `get data - 500 ioException `() = runTest {
        //given
        enqueue {
            networkError()
        }

        //when
        val apiResponse = apiSource.getAccounts()

        //then
        Truth.assertThat(apiResponse).isNotNull()
        Truth.assertThat(apiResponse.isError()).isTrue()
        Truth.assertThat(apiResponse).isInstanceOf(ApiResponse.NetworkError::class.java)
    }

    @Test
    fun `get data - unknown (deserialization) error `() = runTest {
        //given
        enqueue {
            unknownError()
        }

        //when
        val apiResponse = apiSource.getAccounts()

        //then
        Truth.assertThat(apiResponse).isNotNull()
        Truth.assertThat(apiResponse.isError()).isTrue()
        Truth.assertThat(apiResponse).isInstanceOf(ApiResponse.UnexpectedError::class.java)
    }

    companion object {
        var rawAccounts: String? = null
        var dto: List<AccountRaw>? = null
            get() = field!!


        @JvmStatic
        @BeforeClass
        fun loadData() {
            rawAccounts = loadResourceFile("/accounts.json")
            dto = TestUtils.getData<AccountRaw, AccountRaw>("/accounts.json") { this }

        }
    }
}