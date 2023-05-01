package gr.jvoyatz.assignment.wallet.accounts.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.jvoyatz.assignment.wallet.accounts.fakes.HiltFakeAccountRepository
import gr.jvoyatz.assignment.wallet.accounts.fakes.HiltFakeAppDispatchers
import gr.jvoyatz.assignment.wallet.accounts.fakes.HiltFakeNavigator
import gr.jvoyatz.assignment.wallet.common.android.AppDispatchers
import gr.jvoyatz.assignment.wallet.common.android.navigation.Navigator
import gr.jvoyatz.assignment.wallet.domain.repository.AccountsRepository

@Module
@InstallIn(SingletonComponent::class)
object HiltFakeModule {
    @Provides
    fun provideFakeAppFragmentNavigator(): Navigator = HiltFakeNavigator()

    @Provides
    fun provideHiltRepository(): AccountsRepository = HiltFakeAccountRepository()

    @Provides
    fun provideFakeAppDispatchers(): AppDispatchers = HiltFakeAppDispatchers()
}