package gr.jvoyatz.assignment.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import gr.jvoyatz.assignment.core.database.entities.AccountEntity
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
class WalletDatabaseTest {

    private lateinit var walletDao: AccountsDao
    private lateinit var walletDatabase: AccountsDatabase

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
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        walletDatabase = Room.inMemoryDatabaseBuilder(context, AccountsDatabase::class.java).build()
        walletDao = walletDatabase.dao
    }

    @After
    fun tearDown(){
        if(walletDatabase.isOpen)
            walletDatabase.close()
    }

    @Test
    fun addAccountToWalletDb() = runTest{
        //given
        val account = accountEntity

        //when
        walletDao.insertAccount(account)

        //then
        val savedAccounts = walletDao.getAccounts().first()
        Truth.assertThat(savedAccounts.size).isEqualTo(1)
        Truth.assertThat(savedAccounts[0].id).isEqualTo(accountEntity.id)
    }

    @Test
    fun deleteAllAccounts() = runTest{
        //given
        val account = accountEntity
        walletDao.insertAccount(account)
        Truth.assertThat(walletDao.getAccounts().first()).isNotEmpty()

        //when
        walletDao.deleteAccounts()

        //then
        val storedAccounts = walletDao.getAccounts().first()
        Truth.assertThat(storedAccounts).isEmpty()
    }

    @Test
    fun deleteAccountById() = runTest{
        //given
        val account = accountEntity
        walletDao.insertAccount(account)
        Truth.assertThat(walletDao.getAccounts().first()).isNotEmpty()

        //when
        walletDao.deleteAccount(account)

        //then
        val storedAccounts = walletDao.getAccounts().first()
        Truth.assertThat(storedAccounts).isEmpty()
    }

    @Test
    fun getAccountsAsync() = runTest {
        //given
        val account = accountEntity
        walletDao.insertAccount(account)

        val retirementAccount = account.copy(
            accountNumber = 98765,
            accountNickname = null,
            id = "1234-234-2341341234-12342134"
        )
        walletDao.insertAccount(retirementAccount)


        //when
        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO){
            walletDao.getAccounts().collect {
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