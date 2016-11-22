package id.jasoet.vertx.util

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

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

fun String.toJsonObject(): JsonObject {
    val inputStream = InputStreamReader(javaClass.getResourceAsStream(this), StandardCharsets.UTF_8)
    val lines = inputStream.useLines { it.joinToString("") }
    return JsonObject(lines)
}

inline fun <T> mongoTask(operation: ((T?, Throwable?) -> Unit) -> Unit): Promise<T, Exception> {
    val deferred = deferred<T, Exception>()

    val handler: (T?, Throwable?) -> Unit = { value, throwable ->
        if (throwable != null) {
            deferred.reject(Exception(throwable))
        } else if (value == null) {
            deferred.reject(Exception("Value is Null", throwable))
        } else {
            deferred.resolve(value)
        }
    }

    try {
        operation(handler)
    } catch (e: Exception) {
        deferred.reject(e)
    }

    return deferred.promise
}

inline fun <T> vertxTask(operation: (Handler<AsyncResult<T>>) -> Unit): Promise<T, Exception> {
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



