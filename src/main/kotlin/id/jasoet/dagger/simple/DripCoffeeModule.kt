package id.jasoet.dagger.simple

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DripCoffeeModule {
    @Provides
    @Singleton
    fun provideHeater(): Heater {
        return ElectricHeater()
    }
}