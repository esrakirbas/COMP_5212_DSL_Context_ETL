package etl.core.engine

import etl.core.model.CsvSource
import etl.core.model.Extract
import etl.core.model.JsonSource
import etl.core.model.Record
import etl.util.Logger

object ExtractEngine {

    private val logger = Logger.forClass(ExtractEngine::class)

    fun extract(extract: Extract): List<Record> {
        return extract.sources.flatMap { source ->
            when (source) {
                is CsvSource -> {
                    val records = CsvReader.readCsv(source.path)
                    logger.info("Extracted ${records.size} records from ${source.path}")
                    records
                }
                is JsonSource -> {
                    val records = JsonReader.readJson(source.path)
                    logger.info("Extracted ${records.size} records from ${source.path}")
                    records
                }
                else -> emptyList() //TODO change later
                //is ExcelSource -> readExcel(source.path)
            }
        }
    }
}