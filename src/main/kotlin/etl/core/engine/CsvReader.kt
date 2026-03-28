package etl.core.engine

import etl.core.model.Record
import etl.util.Logger
import org.apache.commons.csv.CSVFormat
import java.io.FileReader

object CsvReader {

    private val logger = Logger.forClass(CsvReader::class)

    fun readCsv(path: String): List<Record> {
        logger.info("Opening CSV File : $path")
        FileReader(path).use { reader -> //use guarantees to close the file
            logger.info("CSV File Opened: $path Extracting CSV Records.")
            CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader)
                .use { parser -> //guarantees to close the resources
                    val headers = parser.headerMap.keys
                    return parser.map { csvRecord ->
                        headers.associateWith { header -> csvRecord.get(header) }.toMutableMap()
                    }
                }

        }

    }
}