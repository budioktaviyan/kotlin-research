package id.jasoet.vertx

import id.jasoet.vertx.util.vertxTask
import io.vertx.core.AbstractVerticle
import io.vertx.core.logging.LoggerFactory
import nl.komponents.kovenant.all
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
@Named("mainVerticle")
class MainVerticle @Inject constructor(
    val httpVerticle: HttpVerticle,
    val formUploadVerticle: FormUploadVerticle) : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(MainVerticle::class.java)

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
            log.info("Success Deploy HttpVerticle and FormUploadVerticle")
        } fail {
            log.info("Failed to Deploy HttpVerticle and FormUploadVerticle")
            vertxTask<Void> {
                vertx.close(it)
            }
        }

    }
}
