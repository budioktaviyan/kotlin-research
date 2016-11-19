package id.jasoet.vertx

import id.jasoet.vertx.util.vertxTask
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServer
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import nl.komponents.kovenant.all
import nl.komponents.kovenant.task
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
@Named("formUploadVerticle")
class FormUploadVerticle @Inject constructor() : AbstractVerticle() {
    private val log = LoggerFactory.getLogger(FormUploadVerticle::class.java)
    private val fileSystem by lazy { vertx.fileSystem() }
    private val config by lazy { config() }

    override fun start(startFuture: Future<Void>) {
        val httpServer = vertx.createHttpServer()
        val router = Router.router(vertx)

        router.route().handler(BodyHandler.create().setMergeFormAttributes(true))
        router.get("/").handler { context ->
            context
                .request()
                .response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html")
                .end("/webroot/index.html".resourceToBuffer())
        }

        router.post("/form").handler { context ->
            log.info("Handle Form Uploads....")
            val tempPath = "/var/tmp"
            val fileUploads = context.fileUploads()

            val receiveAndSave = measureTimeMillis {
                val saveFileTasks = fileUploads
                    .filter { it.size() > 0 }
                    .map { fileUpload ->
                        task {
                            val uploadedFile = fileSystem.readFileBlocking(fileUpload.uploadedFileName())
                            log.info("Receive File ${fileUpload.uploadedFileName()} with ${uploadedFile.length()} bytes")
                            val fileName = "$tempPath/${fileUpload.fileName()}"
                            fileSystem.writeFileBlocking(fileName, uploadedFile)
                            fileName
                        }
                    }


                all(saveFileTasks).success {
                    log.info("Success Save all Attachment [${it.joinToString(",")}]")
                }.fail { e ->
                    log.error("Failed to Save Attachments ${e.message}", e)
                }
            }


            context.response().end("Thank You!. Process need $receiveAndSave ms")
        }

        val serverTask = vertxTask<HttpServer> {
            httpServer.requestHandler { router.accept(it) }.listen(8989, it)
        }

        serverTask success {
            log.info("Listen ${config.getInteger("http.port", 8989)}")
            startFuture.complete()
        } fail {
            log.error("Failed to Start HttpServer ${it.message}", it)
            startFuture.fail(it)
        }
    }

    fun String.resourceToBuffer(): Buffer {
        val inputStream = javaClass.getResourceAsStream(this)
        val byteArray = ByteArray(inputStream.available())

        inputStream.use {
            it.read(byteArray)
        }
        return Buffer.buffer(byteArray)
    }
}
