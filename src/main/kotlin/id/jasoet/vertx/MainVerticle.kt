package id.jasoet.vertx

import id.jasoet.vertx.util.vertxTask
import io.vertx.core.AbstractVerticle
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.logging.SLF4JLogDelegateFactory
import nl.komponents.kovenant.all

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

class MainVerticle constructor(
    val httpVerticle: HttpVerticle,
    val formUploadVerticle: FormUploadVerticle) : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(MainVerticle::class.java)

    init {
        System.setProperty(LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory::class.java.name)
    }

    override fun start() {
        val deployTasks = arrayOf(
            vertxTask<String> {
                vertx.deployVerticle(httpVerticle, it)
            },
            vertxTask<String> {
                vertx.deployVerticle(formUploadVerticle, it)
            }
        )
        all(*deployTasks) success {
            println("Success Deploy HttpVerticle and FormUploadVerticle")
        } fail {
            println("Failed to Deploy HttpVerticle and FormUploadVerticle")
            vertxTask<Void> {
                vertx.close(it)
            }
        }

    }
}
