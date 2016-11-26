package id.jasoet.vertx.extension

import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Query

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


inline fun <reified T : Any> Datastore.createQuery(): Query<T> {
    return this.createQuery(T::class.java)
}

inline fun <reified T : Any> Datastore.getOne(id: Any): T? {
    return this.get(T::class.java, id)
}
