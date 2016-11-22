package id.jasoet.vertx.module

import com.mongodb.client.MongoCollection
import dagger.Module
import dagger.Provides
import id.jasoet.vertx.Country
import id.jasoet.vertx.Island
import id.jasoet.vertx.util.endWithJson
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import nl.komponents.kovenant.all
import nl.komponents.kovenant.task
import org.litote.kmongo.MongoOperator.`in`
import org.litote.kmongo.find
import org.litote.kmongo.toList
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
    fun provideHandlerCountries(@Named("countryCollection") collection: MongoCollection<Country>): Handler<RoutingContext> {
        return Handler { routingContext ->
            task {
                log.info("Fetch all Countries")
                collection.find().toList()
            } success {
                routingContext.response().endWithJson(it)
            } fail {
                routingContext.fail(it)
            }
        }
    }

    @Provides
    @Named("islands")
    fun provideHandlerIslands(@Named("islandCollection") collection: MongoCollection<Island>,
                              @Named("countryCollection") countryCollection: MongoCollection<Country>): Handler<RoutingContext> {
        return Handler { routingContext ->
            task {
                log.info("Fetch all Islands")
                val islands = collection.find().toList()

                val countryNames = islands.map { it.country }.distinct()
                val query = "{ _id: { $`in`: ${Json.encode(countryNames)} } }"
                log.info("Fetch Country with query [$query]")
                val countries = countryCollection.find(query).toList()

                islands.map { island ->
                    mapOf(
                        "name" to island.name,
                        "country" to countries.find { it.code == island.country }
                    )
                }
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