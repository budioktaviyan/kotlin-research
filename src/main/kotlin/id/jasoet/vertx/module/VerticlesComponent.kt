package id.jasoet.vertx.module

import dagger.Component
import id.jasoet.vertx.HttpVerticle
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
@Component(modules = arrayOf(HttpModule::class))
internal interface VerticlesComponent {
    fun httpVerticle(): HttpVerticle
}