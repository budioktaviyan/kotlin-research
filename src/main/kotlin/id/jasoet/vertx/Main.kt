package id.jasoet.vertx

import id.jasoet.vertx.module.DaggerAppComponent
import id.jasoet.vertx.module.HttpModule
import id.jasoet.vertx.module.MongoModule
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
    val deploymentOptions = DeploymentOptions().apply {
        config = "/application-config.json".toJsonObject()
    }

    val appComponent = DaggerAppComponent
        .builder()
        .mongoModule(MongoModule(deploymentOptions.config))
        .httpModule(HttpModule(vertx))
        .build()
    val httpVerticle = appComponent.httpVerticle()
    val initializer = appComponent.initializer()
    initializer()
    vertx.deployVerticle(httpVerticle, deploymentOptions)
}