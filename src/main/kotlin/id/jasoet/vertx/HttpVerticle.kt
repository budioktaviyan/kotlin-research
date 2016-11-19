package id.jasoet.vertx

import id.jasoet.vertx.util.vertxTask
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.http.HttpServer
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class HttpVerticle @Inject constructor(
    @Named("root") val handlerRoot: Handler<RoutingContext>,
    @Named("islands") val handlerIslands: Handler<RoutingContext>,
    @Named("countries") val handlerCountries: Handler<RoutingContext>) : AbstractVerticle() {
    private val log = LoggerFactory.getLogger(HttpVerticle::class.java)

    private val config by lazy {
        config()
    }

    override fun start(startFuture: Future<Void>) {
        val router = createRouter()
        val serverTask =
            vertxTask<HttpServer> {
                vertx.createHttpServer()
                    .requestHandler { router.accept(it) }
                    .listen(config.getInteger("http.port", 9000), it)
            }

        serverTask success {
            log.info("Listen ${config.getInteger("http.port", 9000)}")
            startFuture.complete()

        } fail {
            log.error("Failed to Start HttpServer ${it.message}", it)
            startFuture.fail(it)
        }
    }

    private fun createRouter(): Router {
        return Router.router(vertx).apply {
            get("/").handler(handlerRoot)
            get("/islands").handler(handlerIslands)
            get("/countries").handler(handlerCountries)
        }
    }

}
