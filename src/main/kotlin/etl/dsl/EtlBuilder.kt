package etl.dsl

import etl.model.EtlJob
import etl.model.Load
import etl.model.Schema
import etl.model.Source
import etl.model.Transform

/** Main tag **/
@EtlDslMarker
class EtlBuilder {
    private val sources = mutableListOf<Source>()
    private var schema: Schema? = null //allow temporarily incomplete state while constructing. After build() it will be immutable (see EtlJob.kt)
    private var transform: Transform? = null //allow temporarily incomplete state while constructing. After build() it will be immutable (see EtlJob.kt)
    private var load: Load? = null //allow temporarily incomplete state while constructing. After build() it will be immutable (see EtlJob.kt)

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

    fun transform(block: TransformBuilder.() -> Unit) {
        val builder = TransformBuilder()
        builder.block()
        transform = builder.build()
    }

    fun load(block: LoadBuilder.() -> Unit) {
        val builder = LoadBuilder()
        builder.block()
        load = builder.build()
    }

    fun build(): EtlJob {
        return EtlJob(
            sources = sources,
            schema = schema ?: throw DslException("Schema is mandatory"),
            transform = transform ?: throw DslException("Transform is mandatory"),
            load = load ?: throw DslException("Load is mandatory")
        )
    }
}