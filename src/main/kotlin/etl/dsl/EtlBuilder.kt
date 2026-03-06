package etl.dsl

import etl.model.EtlJob
import etl.model.Source

@EtlDslMarker
class EtlBuilder {
    private val sources = mutableListOf<Source>()

    fun extract(block: ExtractBuilder.() -> Unit) {
        val builder = ExtractBuilder()
        builder.block()
        sources.addAll(builder.build())
    }

    fun build(): EtlJob {
        return EtlJob(
            sources = sources
        )
    }
}