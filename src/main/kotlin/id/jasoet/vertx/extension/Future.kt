package id.jasoet.vertx.extension

import io.vertx.core.AsyncResult
import io.vertx.core.CompositeFuture
import io.vertx.core.Future
import io.vertx.core.Handler

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


inline fun <T> futureTask(operation: (Handler<AsyncResult<T>>) -> Unit): Future<T> {
    val future = Future.future<T>()
    operation(future.completer())
    return future
}

@Suppress("NOTHING_TO_INLINE")
inline fun all(vararg futures: Future<*>): CompositeFuture {
    return CompositeFuture.all(futures.asList())
}

@Suppress("NOTHING_TO_INLINE")
inline fun any(vararg futures: Future<*>): CompositeFuture {
    return CompositeFuture.any(futures.asList())
}

infix fun <T> Future<T>.handle(operation: (AsyncResult<T>) -> Unit): Future<T> {
    return this.setHandler(operation)
}

fun <T> Future<T>.handle(success: (T) -> Unit, failed: (Throwable) -> Unit): Future<T> {
    return this.setHandler {
        if (it.succeeded()) {
            success(it.result())
        } else {
            failed(it.cause())
        }
    }
}