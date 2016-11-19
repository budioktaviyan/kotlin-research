package id.jasoet.dagger.simple

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


import dagger.Lazy
import javax.inject.Inject

internal class CoffeeMaker @Inject constructor(
    private val heater: Lazy<Heater>,
    private val pump: Pump) {

    fun brew() {
        heater.get().on()
        pump.pump()
        println(" [_]P coffee! [_]P ")
        heater.get().off()
    }
}
