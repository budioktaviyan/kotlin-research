package id.jasoet.vertx.util

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */



fun <T> vertxTask(operation: (Handler<AsyncResult<T>>) -> Unit): Promise<T, Exception> {
    val deferred = deferred<T, Exception>()

    val handler = Handler<AsyncResult<T>> {
        if (it.succeeded()) {
            deferred.resolve(it.result())
        } else {
            deferred.reject(Exception(it.cause()))
        }
    }

    try {
        operation(handler)
    } catch (e: Exception) {
        deferred.reject(e)
    }

    return deferred.promise
}


