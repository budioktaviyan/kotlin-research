package id.jasoet.vertx.module

import dagger.Module
import dagger.Provides
import id.jasoet.vertx.extension.createQuery
import id.jasoet.vertx.extension.endWithJson
import id.jasoet.vertx.model.Country
import id.jasoet.vertx.model.Island
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import nl.komponents.kovenant.all
import nl.komponents.kovenant.task
import org.mongodb.morphia.Datastore
import javax.inject.Named
import kotlin.system.measureTimeMillis

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


@Module
class HttpModule(val vertx: Vertx) {
    private val log = LoggerFactory.getLogger(HttpModule::class.java)

    @Provides
    @Named("countries")
    fun provideHandlerCountries(datastore: Datastore): Handler<RoutingContext> {
        return Handler { routingContext ->
            task {
                log.info("Fetch all Countries")
                datastore.createQuery<Country>().asList()
            } success {
                routingContext.response().endWithJson(it)
            } fail {
                routingContext.fail(it)
            }
        }
    }

    @Provides
    @Named("islands")
    fun provideHandlerIslands(datastore: Datastore): Handler<RoutingContext> {
        return Handler { routingContext ->
            task {
                log.info("Fetch all Islands")
                datastore.createQuery<Island>().asList()
            } success {
                routingContext.response().endWithJson(it)
            } fail {
                routingContext.fail(it)
            }
        }
    }

    @Provides
    @Named("root")
    fun provideHandlerRoot(): Handler<RoutingContext> {
        return Handler { routingContext ->
            routingContext.reroute("/static/html/index.html")
        }
    }

    @Provides
    @Named("fileUpload")
    fun provideHandlerFileUpload(): Handler<RoutingContext> {
        return Handler { context ->
            log.info("Handle Form Uploads....")
            val tempPath = "/var/tmp"
            val fileUploads = context.fileUploads()
            val fileSystem = vertx.fileSystem()

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
    }

}