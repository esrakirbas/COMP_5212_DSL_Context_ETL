package etl.core.engine

import etl.core.model.*
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

                is ExcelSource -> {
                    val records = ExcelReader.readExcel(source.path)
                    logger.info("Extracted ${records.size} records from ${source.path}")
                    records
                }
            }
        }
    }
}