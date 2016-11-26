package id.jasoet.vertx.model

import org.hibernate.validator.constraints.NotBlank
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */

@Entity("country")
@Indexes(
    Index(value = "code", fields = arrayOf(Field("code")))
)
class Country() {

    constructor(code: String, name: String) : this() {
        this.code = code
        this.name = name
    }

    @Id
    lateinit var code: String
    @NotBlank
    var name: String = ""

    override fun toString(): String {
        return "Country(code=$code, name='$name')"
    }

}