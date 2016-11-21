package id.jasoet.vertx.util

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

fun String.resourceToBuffer(): Buffer {
    val inputStream = javaClass.getResourceAsStream(this)
    val byteArray = ByteArray(inputStream.available())

    inputStream.use {
        it.read(byteArray)
    }
    return Buffer.buffer(byteArray)
}


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


