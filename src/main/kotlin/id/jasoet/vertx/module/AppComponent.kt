package id.jasoet.vertx.module

import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import dagger.Component
import id.jasoet.vertx.HttpVerticle
import javax.inject.Named
import javax.inject.Singleton

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Singleton
@Component(
    modules = arrayOf(HttpModule::class, MongoModule::class)
)
internal interface AppComponent {
    fun mongoClient(): MongoClient
    fun database(): MongoDatabase
    @Named("dataInitializer") fun initializer(): () -> Unit
    fun httpVerticle(): HttpVerticle
}
