package etl.core.engine

import etl.core.model.Record
import etl.util.Logger
import org.apache.commons.csv.CSVFormat
import java.io.File
import java.io.FileReader

object CsvReader {

    private val logger = Logger.forClass(CsvReader::class)

    fun readCsv(path: String): List<Record> {
        logger.info("Opening CSV File : $path")

        val file = File(path)
        if (!file.exists()) {
            logger.error("CSV file not found: $path")
            return emptyList()
        }

        FileReader(file).use { reader -> //use guarantees to close the file
            logger.info("CSV File Opened: $path Extracting CSV Records.")
            CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader)
                .use { parser -> //guarantees to close the resources
                    val headers = parser.headerMap.keys
                    return parser.mapIndexed { index, csvRecord ->
                        val record = headers.associateWith { header -> csvRecord.get(header) }.toMutableMap()
                        record["_source"] = path
                        record["_recIndex"] = (index+1).toString()
                        record
                    }
                }
        }
    }
}