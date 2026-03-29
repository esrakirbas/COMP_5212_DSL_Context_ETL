package etl.core.engine

import etl.core.model.Record
import etl.util.Logger
import tools.jackson.databind.ObjectMapper
import java.io.File

object JsonReader {

    private val logger = Logger.forClass(JsonReader::class)
    private val mapper = ObjectMapper()

    fun readJson(path: String): List<Record> {
        logger.info("Opening JSON File : $path")

        val file = File(path)

        if (!file.exists()) {
            logger.error("JSON file not found: $path")
            return emptyList()
        }

        val data: List<Map<String, Any?>> =
            mapper.readValue(file, mapper.typeFactory.constructCollectionType(List::class.java, Map::class.java))

        logger.info("JSON File Opened: $path Extracting JSON Records.")

        val records = data.mapIndexed { index, map ->
            val record = mutableMapOf<String, String>()

            for ((key, value) in map) {
                record[key] = value?.toString() ?: ""
            }

            record["_source"] = path
            record["_recIndex"] = (index+1).toString()
            record
        }

        return records

    }
}