package gr.jvoyatz.assignment.wallet.di

import android.os.Looper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gr.jvoyatz.assignment.wallet.navigation.NavigatorImpl
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
@InstallIn(SingletonComponent::class)
@Module
object WalletModule {
    @Singleton
    @Provides
    fun provideHandler() = android.os.Handler(Looper.getMainLooper())

    @Singleton
    @Provides
    fun provideExecutor(): Executor = Executors.newSingleThreadExecutor()

    @Singleton
    @Provides
    fun provideNavigator(): gr.jvoyatz.assignment.wallet.common.android.navigation.Navigator {
        return NavigatorImpl()
    }
}