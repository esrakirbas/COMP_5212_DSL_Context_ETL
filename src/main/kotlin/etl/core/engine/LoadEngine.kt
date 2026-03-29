package etl.core.engine
import etl.core.dsl.DslException
import etl.core.model.Load
import etl.core.model.Record
import etl.util.Logger
import java.io.File
import etl.core.model.Csv

object LoadEngine {

    private val logger = Logger.forClass(LoadEngine::class)

    fun load(records: List<Record>, load: Load) {
        writeCsv(records, load)
    }

    fun load(records: List<Record>, rejectedOutputPath: String) {
        val tempLoad = Load(
            csv = Csv(
                fileName = rejectedOutputPath,
                overwrite = true //rejected file
            )
        )

        writeCsv(records, tempLoad)
    }

    private fun writeCsv(records: List<Record>, load: Load) {
        if (records.isEmpty()) {
            logger.warn("No records to write. I will not to do any process on file : ${load.csv.fileName}")
            return
        }

        val file = File(load.csv.fileName)
        logger.info("Trying to locate or create the file : ${file.absolutePath}")

        if (file.exists() && !load.csv.overwrite) {
            throw DslException("[${this::class.simpleName}]File already exists and overwrite is disabled: ${load.csv.fileName}")
        }

        if(file.exists()) {
            logger.info("File already exists and will be overridden after load: ${load.csv.fileName}")
        }

        val headers = records.first().keys

        file.bufferedWriter().use { writer -> //'use' guarantees the file will be closed

            writer.write(headers.joinToString(","))
            writer.newLine()

            records.forEach { record ->
                val row = headers.joinToString(",") { field ->
                    record[field] ?: ""
                }
                writer.write(row)
                writer.newLine()
            }
        }

        logger.info("OK: wrote ${records.size} records to ${load.csv.fileName}")
    }
}