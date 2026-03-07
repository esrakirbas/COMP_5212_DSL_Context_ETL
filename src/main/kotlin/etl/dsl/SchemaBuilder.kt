package etl.dsl

import etl.model.Field
import etl.model.Schema

@EtlDslMarker
class SchemaBuilder {
    private val fields = mutableListOf<Field>()

    fun field(name: String, block: FieldBuilder.() -> Unit) {
        if(fields.any { it.name == name }) {
            throw DslException("Field $name is already defined in schema.")
        }
        val builder = FieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    fun build(): Schema {
        if (fields.isEmpty()) {
            throw DslException("No fields defined in schema.")
        }
        return Schema(fields)
    }
}