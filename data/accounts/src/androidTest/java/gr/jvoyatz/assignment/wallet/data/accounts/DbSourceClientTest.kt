package gr.jvoyatz.assignment.wallet.data.accounts

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import gr.jvoyatz.assignment.core.database.AccountsDao
import gr.jvoyatz.assignment.core.database.AccountsDatabase
import gr.jvoyatz.assignment.core.database.entities.AccountEntity
import gr.jvoyatz.assignment.wallet.data.accounts.internal.DbSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@Suppress("IllegalIdentifier")
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DbSourceClientTest {

    private lateinit var walletDao: AccountsDao
    private lateinit var walletDatabase: AccountsDatabase
    private lateinit var sut: DbSourceImpl

    private val accountEntity = AccountEntity(
        "1f34c76a-b3d1-43bc-af91-a82716f1bc2e",
        12345,
        "current",
        "99.00",
        "EUR",
        "my salary",
        beneficiaries = "",
        branch = "",
        openedDate = "",
        productName = ""
    )


    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        walletDatabase = Room.inMemoryDatabaseBuilder(context, AccountsDatabase::class.java).build()
        walletDao = walletDatabase.dao
        sut = DbSourceImpl(walletDao)
    }

    @After
    fun tearDown() {
        if (walletDatabase.isOpen)
            walletDatabase.close()
    }

    @Test
    fun addFavoriteAccount() = runTest {
        //given
        val account = accountEntity

        //when
        sut.addFavoriteAccount(account)

        //then
        val savedAccounts = sut.getAccounts().first()
        Truth.assertThat(savedAccounts.size).isEqualTo(1)
        Truth.assertThat(savedAccounts[0].id).isEqualTo(accountEntity.id)
    }


    @Test
    fun isFavoriteAccount() = runTest {
        //given
        val account = accountEntity
        sut.addFavoriteAccount(account)

        //when
        val isFavorite = sut.isFavorite(account.id)

        //then
        Truth.assertThat(isFavorite).isTrue()
    }

    @Test
    fun getAccountById() = runTest {
        //given
        val account = accountEntity
        sut.addFavoriteAccount(account)

        //when
        val dbAccount = sut.getAccountById(account.id)

        //then
        Truth.assertThat(dbAccount).isNotEmpty()
    }

    @Test
    fun removeFavoriteAccount() = runTest {
        //given
        val account = accountEntity
        sut.addFavoriteAccount(account)


        //when
        sut.removeFavoriteAccount(account)

        //then
        val savedAccounts = sut.getAccounts().first()
        Truth.assertThat(savedAccounts.size).isEqualTo(0)
    }

    @Test
    fun deleteAllAccounts() = runTest {
        //given
        val account = accountEntity
        sut.addFavoriteAccount(account)
        Truth.assertThat(walletDao.getAccounts().first()).isNotEmpty()

        //when
        sut.deleteAccounts()

        //then
        val storedAccounts = sut.getAccounts().first()
        Truth.assertThat(storedAccounts).isEmpty()
    }

    @Test
    fun getAccountsAsync() = runTest {
        //given
        val account = accountEntity
        sut.addFavoriteAccount(account)

        val retirementAccount = account.copy(
            accountNumber = 98765,
            accountNickname = null,
            id = "1234-234-2341341234-12342134"
        )
        sut.addFavoriteAccount(retirementAccount)


        //when
        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            sut.getAccounts().collect {
                latch.countDown()
                Truth.assertThat(it).isNotEmpty()
                Truth.assertThat(it.size).isEqualTo(2)
            }
        }

        @Suppress("BlockingMethodInNonBlockingContext")
        latch.await()
        job.cancelAndJoin()
    }
}