package gr.jvoyatz.assignment.wallet.core.api

import com.google.common.truth.Truth
import com.squareup.moshi.JsonDataException
import gr.jvoyatz.assignment.core.testing.utils.TestUtils
import gr.jvoyatz.assignment.core.testing.utils.TestUtils.loadResourceFile
import gr.jvoyatz.assignment.wallet.core.api.models.AccountRaw
import gr.jvoyatz.sportspot.core.testing.utils.ApiServer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException


@ExperimentalCoroutinesApi
class WalletApiClientTest : ApiServer<WalletApi>() {
    init {
        clazz = WalletApi::class.java
    }

    @Test
    fun `get data - 200 OK `() = runTest {
        //given
        enqueue {
            success(rawAccounts!!)
        }

        //when
        val response = apiClient.getAccounts()


        //then
        Truth.assertThat(response).isNotNull()
        Truth.assertThat(response).isNotEmpty()
    }

    @Test
    fun `get data - 400 httpError `() = runTest {
        //given
        enqueue {
            httpError()
        }

        try {
            //when
            apiClient.getAccounts()
        } catch (e: Exception) {
            Truth.assertThat(e).isInstanceOf(HttpException::class.java)
            Truth.assertThat((e as HttpException).code()).isEqualTo(400)
        }
    }


    @Test
    fun `get data - 500 ioException `() = runTest {
        //given
        enqueue {
            networkError()
        }

        try {
            //when
            apiClient.getAccounts()
        } catch (e: Exception) {
            Truth.assertThat(e).isInstanceOf(IOException::class.java)
        }
    }

    @Test
    fun `get data - unknown (deserialization) error `() = runTest {
        //given
        enqueue {
            unknownError()
        }

        try {
            //when
            apiClient.getAccounts()
        } catch (e: Exception) {
            Truth.assertThat(e).isInstanceOf(JsonDataException::class.java)
        }
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