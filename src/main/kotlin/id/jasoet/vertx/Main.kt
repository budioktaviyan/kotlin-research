package id.jasoet.vertx

import id.jasoet.vertx.module.DaggerVerticlesComponent
import id.jasoet.vertx.module.HttpModule
import id.jasoet.vertx.util.toJsonObject
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.logging.SLF4JLogDelegateFactory

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

fun main(args: Array<String>) {
    System.setProperty(LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory::class.java.name)
    val vertx = Vertx.vertx()
    val httpVerticle = DaggerVerticlesComponent.builder().httpModule(HttpModule(vertx)).build().httpVerticle()
    val deploymentOptions = DeploymentOptions().apply {
        config = "/application-config.json".toJsonObject()
    }
    vertx.deployVerticle(httpVerticle, deploymentOptions)
}