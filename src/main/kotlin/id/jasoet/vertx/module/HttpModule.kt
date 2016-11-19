package id.jasoet.vertx.module

import dagger.Module
import dagger.Provides
import id.jasoet.vertx.Country
import id.jasoet.vertx.Island
import id.jasoet.vertx.util.endWithJson
import io.vertx.core.Handler
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import javax.inject.Named

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


@Module
class HttpModule {
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
        return Handler { req ->
            req.response().endWithJson(mockIslands)
        }
    }

    @Provides
    @Named("root")
    fun provideHandlerRoot(): Handler<RoutingContext> {
        return Handler { req ->
            req.response().end("Welcome!")
        }
    }

    @Provides
    @Named("countries")
    fun provideHandlerCountries(mockIslands: List<Island>): Handler<RoutingContext> {
        return Handler { req ->
            req.response().endWithJson(mockIslands.map { it.country }.distinct().sortedBy { it.code })
        }
    }

}