package etl.core.engine

import etl.core.model.EtlJob

class EtlRunner {

    fun run(job: EtlJob) {
        val logger: ((String) -> Unit)? = job.log?.let { logConfig ->
            { message: String ->
                println(message) // temporary behavior
                // later → we will write to log file using logConfig.path
            }
        }
        val extractedData = ExtractEngine.extract(job.extract)
        logger?.invoke("Extracted rows: ${extractedData.size}")

        val validationResult = SchemaEngine.validate(extractedData, job.schema, logger)
        // validationResult logging
        logger?.invoke("Accepted record found: ${validationResult.acceptedRecords.size}")
        logger?.invoke("Invalid record count: ${validationResult.invalidCount}")
        logger?.invoke("Rejected record found: ${validationResult.rejectedCount}")
        logger?.invoke("Error totals by field:")
        validationResult.fieldErrors.forEach { (field, count) ->
            logger?.invoke("Field: $field → $count errors")
        }
    }
}