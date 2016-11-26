package id.jasoet.vertx

import id.jasoet.vertx.extension.futureTask
import id.jasoet.vertx.extension.handle
import id.jasoet.vertx.router.MainRouter
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServer
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
@Named("httpVerticle")
class HttpVerticle @Inject constructor(val mainRouter: MainRouter) : AbstractVerticle() {
    private val log = LoggerFactory.getLogger(HttpVerticle::class.java)

    private val config by lazy {
        config()
    }

    override fun start(startFuture: Future<Void>) {
        val router = mainRouter.execute(Router.router(vertx))
        val serverTask =
            futureTask<HttpServer> {
                vertx.createHttpServer()
                    .requestHandler { router.accept(it) }
                    .listen(config.getInteger("http.port", 9000), it)
            }

        serverTask handle  {
            if(it.succeeded()){
                log.info("Listen ${config.getInteger("http.port", 9000)}")
                startFuture.complete()
            }else{
                log.error("Failed to Start HttpServer ${it.cause().message}", it)
                startFuture.fail(it.cause())
            }

        }
    }

}
