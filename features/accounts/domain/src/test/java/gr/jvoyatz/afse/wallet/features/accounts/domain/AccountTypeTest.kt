package gr.jvoyatz.afse.wallet.features.accounts.domain


import com.google.common.truth.Truth
import gr.jvoyatz.afse.wallet.common.android.domain.models.AccountType
import org.junit.Test

class AccountTypeTest {


    @Test
    fun `test account type util method operator returns type`(){
        //given
        val current = "current"

        //when
        val currentAccountType = AccountType["current"]

        //then
        Truth.assertThat(AccountType.CURRENT).isEqualTo(currentAccountType)
    }

    @Test
    fun `test account type util method operator returns NONE`(){
        //given
        val current = "current"

        //when
        val currentAccountType = AccountType["current234"]

        //then
        Truth.assertThat(AccountType.NONE).isEqualTo(currentAccountType)
    }

    @Test
    fun `test account type util method infix returns type`(){
        //given
        val current = "current"

        //when
        val currentAccountType = AccountType from "current"

        //then
        Truth.assertThat(AccountType.CURRENT).isEqualTo(currentAccountType)
    }

    @Test
    fun `test account type util method infix returns none`(){
        //given
        val current = "current"

        //when
        val currentAccountType = AccountType from "111!crrent"

        //then
        Truth.assertThat(AccountType.NONE).isEqualTo(currentAccountType)
    }
}