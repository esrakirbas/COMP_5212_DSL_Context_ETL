package etl.core.engine

import etl.core.model.CsvSource
import etl.core.model.Extract
import etl.core.model.Record

object ExtractEngine {
    fun extract(extract: Extract): List<Record> {
        return extract.sources.flatMap { source ->
            when (source) {
                is CsvSource -> CsvReader.readCsv(source.path)
                else -> emptyList() //TODO change later
                //is JsonSource -> readJson(source.path)
                //is ExcelSource -> readExcel(source.path)
            }
        }
    }
}