package etl.core.engine

import etl.core.model.Record
import org.apache.commons.csv.CSVFormat
import java.io.FileReader

object CsvReader {
    fun readCsv(path: String): List<Record> {
        val reader = FileReader(path)
        val parser = CSVFormat.DEFAULT
            .withFirstRecordAsHeader()
            .parse(reader)

        val headers = parser.headerMap.keys

        return parser.map { csvRecord ->
            headers.associateWith { header ->
                csvRecord.get(header)
            }.toMutableMap()
        }
    }
}