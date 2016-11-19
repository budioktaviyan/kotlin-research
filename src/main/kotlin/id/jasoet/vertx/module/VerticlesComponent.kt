package id.jasoet.vertx.module

import dagger.Component
import id.jasoet.vertx.FormUploadVerticle
import id.jasoet.vertx.HttpVerticle
import id.jasoet.vertx.MainVerticle
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
    fun formUploadVerticle(): FormUploadVerticle
    fun mainVerticle(): MainVerticle
}