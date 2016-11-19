package id.jasoet.vertx.util

import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


/**
 * Extension to the HTTP response to output JSON objects.
 */
fun HttpServerResponse.endWithJson(obj: Any) {
    this.putHeader("Content-Type", "application/json; charset=utf-8")
        .end(Json.encodePrettily(obj))
}
