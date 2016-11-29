package id.jasoet.vertx.module

import com.mongodb.MongoClient
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import dagger.Module
import dagger.Provides
import id.jasoet.vertx.extension.createQuery
import id.jasoet.vertx.extension.getOne
import id.jasoet.vertx.model.Country
import id.jasoet.vertx.model.Island
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia
import javax.inject.Named


/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Module(includes = arrayOf(VertxModule::class))
class MongoModule(val config: JsonObject) {
    private val log = LoggerFactory.getLogger(MongoModule::class.java)

    @Provides
    fun provideMongoClient(): MongoClient {
        val defaultConfig = JsonArray(
            listOf(JsonObject(mapOf("host" to "localhost", "port" to 27017)))
        )
        val mongodbConfig = config.getJsonArray("mongodb.servers", defaultConfig)

        val servers = mongodbConfig.filter { it is JsonObject }.map {
            if (it is JsonObject) {
                ServerAddress(it.getString("host"), it.getInteger("port"))
            } else {
                throw IllegalArgumentException("Config is not JsonObject")
            }
        }

        return MongoClient(servers)
    }

    @Provides
    fun provideMorphia(): Morphia {
        return Morphia().apply {
            mapPackage("id.jasoet.vertx.model")
        }
    }

    @Provides
    fun provideMorphiaDataStore(morphia: Morphia, client: MongoClient): Datastore {
        return morphia
            .createDatastore(client, config.getString("mongodb.database"))
            .apply {
                ensureIndexes()
            }
    }

    @Provides
    fun providesMongoDatabase(client: MongoClient): MongoDatabase {
        val databaseName = config.getString("mongodb.database")
        return client.getDatabase(databaseName)
    }


    @Provides
    @Named("dataInitializer")
    fun provideInitializer(datastore: Datastore): () -> Unit {

        val operation: () -> Unit = {
            val countries = datastore.createQuery<Country>().asList()
            if (countries.isEmpty()) {
                log.info("Countries Empty, Populate Data")
                datastore.save(
                    Country("id", "Indonesia"),
                    Country("my", "Malaysia"),
                    Country("sg", "Singapore"),
                    Country("uk", "United Kingdom"),
                    Country("us", "United States")
                )
            }

            val islands = datastore.createQuery<Island>().asList()
            if (islands.isEmpty()) {
                log.info("Islands Empty, Populate Data")
                val indonesia = datastore.getOne<Country>("id") ?: throw Exception("Country [id] not Found!")
                datastore.save(
                    Island(name = "Java", country = indonesia),
                    Island(name = "Borneo", country = indonesia),
                    Island(name = "Bali", country = indonesia),
                    Island(name = "Lombok", country = indonesia)
                )
            }
        }

        return operation
    }

}
