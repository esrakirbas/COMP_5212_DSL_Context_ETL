package etl.dsl

import etl.model.EtlJob

/**
 * ETL entry point
 */
fun etl(block: EtlBuilder.() -> Unit): EtlJob {
    val builder = EtlBuilder()
    builder.block()
    return builder.build()
}