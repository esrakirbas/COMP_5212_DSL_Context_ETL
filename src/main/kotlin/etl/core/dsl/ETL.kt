package etl.core.dsl

import etl.core.model.EtlJob

/**
 * ETL entry point
 */
fun etl(block: EtlBuilder.() -> Unit): EtlJob {
    val builder = EtlBuilder()
    builder.block()
    return builder.build()
}