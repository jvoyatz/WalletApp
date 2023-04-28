package gr.jvoyatz.assignment.core.database

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {
    @Provides
    @Singleton
    fun provideWalletDao(@ApplicationContext context: Context, executor: Executor) = DatabaseProvider.getWalletDao(context, executor)
}