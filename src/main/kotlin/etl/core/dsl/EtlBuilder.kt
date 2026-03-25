package etl.core.dsl

import etl.core.model.*

/** Main tag **/
@EtlDslMarker
class EtlBuilder {
    //For the following variables, we allow temporarily incomplete state while constructing. After build() they will be immutable (see EtlJob.kt)
    private var extract: Extract? = null
    private var schema: Schema? = null
    private var transform: Transform? = null
    private var load: Load? = null
    private var log: Log? = null

    fun extract(block: ExtractBuilder.() -> Unit) {
        extract = ExtractBuilder().apply(block).build()
    }

    fun schema(block: SchemaBuilder.() -> Unit) {
        schema = SchemaBuilder().apply(block).build()
    }

    fun transform(block: TransformBuilder.() -> Unit) {
        transform = TransformBuilder().apply(block).build()
    }

    fun load(block: LoadBuilder.() -> Unit) {
        load = LoadBuilder().apply(block).build()
    }

    fun log(fileAbsolutePath: String) {
        this.log = Log(fileAbsolutePath)
    }

    fun build(): EtlJob {
        return EtlJob(
            extract = extract ?: throw DslException("Extract is mandatory"),
            schema = schema ?: throw DslException("Schema is mandatory"),
            transform = transform,
            load = load ?: throw DslException("Load is mandatory"),
            log = log
        )
    }
}