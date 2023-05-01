package gr.jvoyatz.assignment.wallet.accounts

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import gr.jvoyatz.assignment.wallet.accounts.hilt.launchFragmentInHiltContainer
import gr.jvoyatz.assignment.wallet.accounts.utils.EspressoUtils.idleFor
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AccountsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun testThatRecyclerViewIsVisibleAfterDelay(){
        launchFragmentInHiltContainer<AccountsFragment>()

        onView(isRoot()).perform(idleFor(3000))
        onView(withId(R.id.dataList)).check(matches(isDisplayed()))
    }

    @Test
    fun testThatRecyclerViewItemsAreLoaded(){
        launchFragmentInHiltContainer<AccountsFragment>()

        onView(isRoot()).perform(idleFor(3000))
        onView(withId(R.id.dataList)).check(matches(isDisplayed()))
    }

}