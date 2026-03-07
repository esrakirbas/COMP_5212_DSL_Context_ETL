package etl.dsl

import etl.model.CsvSource
import etl.model.ExcelSource
import etl.model.JsonSource
import etl.model.Source

/** Extract tag . Extract from multiple Sources csv. json and excel **/
@EtlDslMarker
class ExtractBuilder {
    private val sources = mutableListOf<Source>()

    fun csv(path: String) {
        sources.add(CsvSource(path))
    }

    fun json(path: String) {
        sources.add(JsonSource(path))
    }

    fun excel(path: String) {
        sources.add(ExcelSource(path))
    }

    fun build(): List<Source> {
        return sources
    }
}