package gr.jvoyatz.afse.core.di

import android.os.Looper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    @Module
    @InstallIn(SingletonComponent::class)
    interface Bindinds{

    }
}