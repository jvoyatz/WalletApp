package gr.jvoyatz.afse.core.di

import android.content.Context
import android.os.Looper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gr.jvoyatz.afse.core.database.DatabaseProvider
import gr.jvoyatz.afse.core.navigation.Navigator
import gr.jvoyatz.afse.wallet.core.api.WalletApi
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

/**
 * Dagger Hilt module class declared as object for optimization reasons
 * @see [this](https://developer.android.com/codelabs/android-hilt#6)
 * Using object, you create a single instance of this module and you use this instance to access
 * the class methods. Otherwise a new instance will be generated each time a method of this class
 * gets invoked.
 *
 * an alternative would be to use a companion object inside the module and
 * provide methods with @JvmStatic annotation.
 * This way you tell the compiler that it should generate a static function for the dependency we
 * want to provide.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHandler() = android.os.Handler(Looper.getMainLooper())

    @Singleton
    @Provides
    fun provideExecutor(): Executor = Executors.newSingleThreadExecutor()

    @Provides
    fun provideAppFragmentNavigator(): Navigator.FragmentNavigator = AppFragmentNavigator()

    @Provides
    @Singleton
    fun provideWalletApi(@ApplicationContext context: Context) = WalletApi.create(context)

    @Provides
    @Singleton
    fun provideWalletDao(@ApplicationContext context: Context, executor: Executor) =
        DatabaseProvider.getWalletDao(context, executor)

    @Module
    @InstallIn(SingletonComponent::class)
    interface Bindings{

//        @Binds
//        fun bindAppFragmentNavigator(appFragmentNavigator: AppFragmentNavigator) : Navigator.FragmentNavigator
    }
}