package id.jasoet.vertx.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.Reference

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Entity("island")
@Indexes(
    Index(value = "name", fields = arrayOf(Field("name")))
)
class Island() {
    constructor(name: String, country: Country) : this() {
        this.name = name
        this.country = country
    }

    @Id
    var id: ObjectId? = null
    lateinit var name: String
    @Reference
    lateinit var country: Country

    override fun toString(): String {
        return "Island(id=$id, name='$name', country=$country)"
    }

}