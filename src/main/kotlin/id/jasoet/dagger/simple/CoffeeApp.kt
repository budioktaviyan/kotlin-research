package id.jasoet.dagger.simple

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

class CoffeeApp {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            DaggerCoffeeComponent.create().maker().brew()
        }
    }
}
