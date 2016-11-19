package id.jasoet.dagger.simple

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


internal interface Heater {
    fun on()
    fun off()
    val isHot: Boolean
}