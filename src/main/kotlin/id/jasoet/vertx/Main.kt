package id.jasoet.vertx

import id.jasoet.vertx.module.DaggerVerticlesComponent
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
    val mainVerticle = DaggerVerticlesComponent.create().mainVerticle()
    Vertx.vertx().deployVerticle(mainVerticle)
}