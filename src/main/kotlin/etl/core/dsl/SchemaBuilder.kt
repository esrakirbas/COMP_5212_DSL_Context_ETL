package etl.core.dsl

import etl.core.model.Field
import etl.core.model.Schema

@EtlDslMarker
class SchemaBuilder {
    private val fields = mutableListOf<Field>()

    fun field(name: String, block: FieldBuilder.() -> Unit) {
        if(fields.any { it.name == name }) {
            throw DslException("Field $name is already defined in schema.")
        }
        fields += FieldBuilder(name).apply(block).build()
    }

    fun build(): Schema {
        if (fields.isEmpty()) {
            throw DslException("No fields defined in schema.")
        }
        return Schema(fields)
    }
}