package id.jasoet.vertx

import org.bson.types.ObjectId
import org.litote.kmongo.MongoId

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


data class Country(val _id: String, val name: String, val code: String)

data class Island(@MongoId val id: ObjectId? = null, val name: String, val country: String)
