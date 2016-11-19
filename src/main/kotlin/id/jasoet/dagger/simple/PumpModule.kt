package id.jasoet.dagger.simple

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


import dagger.Binds
import dagger.Module

@Module
internal abstract class PumpModule {
    @Binds
    internal abstract fun providePump(pump: Thermosiphon): Pump
}
