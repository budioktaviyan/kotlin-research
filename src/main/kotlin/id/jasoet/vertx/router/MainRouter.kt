package id.jasoet.vertx.router

import id.jasoet.vertx.controller.MainController
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler
import javax.inject.Inject

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


class MainRouter @Inject constructor(val mainController: MainController) {

    fun execute(router: Router):Router {
        return router.apply {
            route("/static/*").handler(StaticHandler.create())

            get("/").handler(mainController.handlerRoot())
            get("/islands").handler(mainController.handlerIslands())
            get("/countries").handler(mainController.handlerCountries())

            post("/form").handler(BodyHandler.create().setMergeFormAttributes(true))
            post("/form").handler(mainController.handlerFileUpload())
        }
    }
}