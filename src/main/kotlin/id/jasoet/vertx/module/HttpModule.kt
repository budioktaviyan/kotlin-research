package id.jasoet.vertx.module

import dagger.Module
import dagger.Provides
import id.jasoet.vertx.Country
import id.jasoet.vertx.Island
import id.jasoet.vertx.util.endWithJson
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import nl.komponents.kovenant.all
import nl.komponents.kovenant.task
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
    fun provideIslands(): List<Island> {
        return listOf(
            Island("Kotlin", Country("Russia", "RU")),
            Island("Stewart Island", Country("New Zealand", "NZ")),
            Island("Cockatoo Island", Country("Australia", "AU")),
            Island("Tasmania", Country("Australia", "AU"))
        )
    }

    @Provides
    @Named("islands")
    fun provideHandlerIslands(mockIslands: List<Island>): Handler<RoutingContext> {
        return Handler { routingContext ->
            routingContext.response().endWithJson(mockIslands)
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

    @Provides
    @Named("countries")
    fun provideHandlerCountries(mockIslands: List<Island>): Handler<RoutingContext> {
        return Handler { routingContext ->
            val countries = mockIslands.map { it.country }.distinct().sortedBy { it.code }
            routingContext.response().endWithJson(countries)
        }
    }

}