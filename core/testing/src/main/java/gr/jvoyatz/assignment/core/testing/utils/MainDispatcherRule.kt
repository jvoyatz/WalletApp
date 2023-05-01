package gr.jvoyatz.assignment.core.testing.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description


/**
 * Used to replace the actual [Dispatchers.IO] value in tests,
 * by utilizing a capability provided by the Junit Library
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(
    /*private*/ val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}


//alternative
fun runWithTestDispatcher(testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
                          testBlock: suspend TestScope.() -> Unit,
): TestResult {
    Dispatchers.setMain(testDispatcher)

    return runTest {
        testBlock()
        Dispatchers.resetMain()
    }
}
