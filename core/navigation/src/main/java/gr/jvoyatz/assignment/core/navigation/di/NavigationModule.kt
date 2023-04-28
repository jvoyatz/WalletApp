package gr.jvoyatz.assignment.core.navigation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NavigationModule {
    @Singleton
    @Provides
    fun provideNavigator(): gr.jvoyatz.assignment.wallet.common.android.navigation.Navigator {
        return NavigatorImpl()
    }
}