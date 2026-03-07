package etl.dsl

import etl.model.EtlJob
import etl.model.Schema
import etl.model.Source

/** Main tag **/
@EtlDslMarker
class EtlBuilder {
    private val sources = mutableListOf<Source>()
    private var schema: Schema? = null //allow temporarily incomplete state while constructing. After build() it will be immutable (see EtlJob.kt)

    fun extract(block: ExtractBuilder.() -> Unit) {
        val builder = ExtractBuilder()
        builder.block()
        sources.addAll(builder.build())
    }

    fun schema(block: SchemaBuilder.() -> Unit) {
        val builder = SchemaBuilder()
        builder.block()
        schema = builder.build()
    }

    fun build(): EtlJob {
        return EtlJob(
            sources = sources,
            schema = schema ?: throw DslException("Schema is mandatory")
        )
    }
}