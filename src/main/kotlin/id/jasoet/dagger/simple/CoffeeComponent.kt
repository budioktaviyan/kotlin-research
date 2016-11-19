package id.jasoet.dagger.simple

import dagger.Component
import javax.inject.Singleton


/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


@Singleton
@Component(modules = arrayOf(DripCoffeeModule::class, PumpModule::class))
internal interface CoffeeComponent {
    fun maker(): CoffeeMaker
}