package gr.jvoyatz.assignment.core.navigation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.jvoyatz.assignment.core.navigation.Navigator
import gr.jvoyatz.assignment.core.navigation.NavigatorImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NavigationModule {
    @Singleton
    @Provides
    fun provideNavigator():Navigator{
        return NavigatorImpl()
    }
}