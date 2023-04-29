package gr.jvoyatz.assignment.wallet.common.android

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface AppDispatchers{
    val io: CoroutineDispatcher
        get() = Dispatchers.IO

    val main: CoroutineDispatcher
        get() = Dispatchers.Main

    val default: CoroutineDispatcher
        get() = Dispatchers.Default

    val unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}

class AppDispatchersImpl @Inject constructor(): AppDispatchers