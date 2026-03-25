package etl.core.dsl

import etl.core.model.Csv
import etl.core.model.Load


@EtlDslMarker
class LoadBuilder {
    var csv: Csv? = null

    fun csv(fileName: String, block: CsvBuilder.() -> Unit) {
        if(!fileName.endsWith(".csv")) throw DslException("Only .csv files are supported")
        if (csv != null) throw DslException("csv is already initialized, only one csv allowed!")
        val builder = CsvBuilder(fileName)
        builder.block()
        csv = builder.build()
    }

    fun build(): Load {
        return Load(
            csv = csv ?: throw DslException("Csv is mandatory"),
        )
    }
}