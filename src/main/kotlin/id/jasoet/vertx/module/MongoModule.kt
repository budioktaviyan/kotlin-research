package id.jasoet.vertx.module

import com.mongodb.MongoClient
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import dagger.Module
import dagger.Provides
import id.jasoet.vertx.Country
import id.jasoet.vertx.Island
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.litote.kmongo.toList
import javax.inject.Named


/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Module
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

        return KMongo.createClient(servers)
    }

    @Provides
    fun providesMongoDatabase(client: MongoClient): MongoDatabase {
        val databaseName = config.getString("mongodb.database")
        return client.getDatabase(databaseName)
    }


    @Provides
    @Named("countryCollection")
    fun providesCountryCollection(database: MongoDatabase): MongoCollection<Country> {
        return database.getCollection<Country>()
    }

    @Provides
    @Named("islandCollection")
    fun providesIslandCollection(database: MongoDatabase): MongoCollection<Island> {
        return database.getCollection<Island>()
    }

    @Provides
    @Named("dataInitializer")
    fun provideInitializer(@Named("countryCollection") countryCollection: MongoCollection<Country>,
                           @Named("islandCollection") islandCollection: MongoCollection<Island>): () -> Unit {

        val operation: () -> Unit = {
            val countries = countryCollection.find().toList()
            if (countries.isEmpty()) {
                log.info("Countries Empty, Populate Data")
                countryCollection.insertMany(listOf(
                    Country("id", "Indonesia", "id"),
                    Country("my", "Malaysia", "my"),
                    Country("sg", "Singapore", "sg"),
                    Country("uk", "United Kingdom", "uk"),
                    Country("us", "United States", "us")
                ))
            }

            val islands = islandCollection.find().toList()
            if (islands.isEmpty()) {
                log.info("Islands Empty, Populate Data")
                islandCollection.insertMany(listOf(
                    Island(name = "Java", country = "id"),
                    Island(name = "Borneo", country = "id"),
                    Island(name = "Bali", country = "id"),
                    Island(name = "Lombok", country = "id")
                ))
            }
        }

        return operation
    }

}
