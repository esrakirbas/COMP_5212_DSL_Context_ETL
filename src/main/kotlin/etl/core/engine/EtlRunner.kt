package etl.core.engine

import etl.core.model.EtlJob
import etl.util.Logger
import java.io.File

class EtlRunner {


    fun run(job: EtlJob) {
        //set logger
        val logger = Logger.forClass(EtlRunner::class)
        if (job.log != null && job.log.fileAbsolutePath.isNotEmpty()) {
            Logger.logFile = File(job.log.fileAbsolutePath)
        }

        //start pipeline
        val extractedData = ExtractEngine.extract(job.extract)
        val validationResult = SchemaEngine.validate(extractedData, job.schema)
        logger.info("Accepted record found: ${validationResult.acceptedRecords.size}")
        logger.info("Invalid record count: ${validationResult.invalidCount}")
        logger.info("Rejected record found: ${validationResult.rejectedCount}")
        logger.info("Error totals by field:")
        validationResult.fieldErrors.forEach { (field, count) ->
            logger.info("Field: $field → $count errors")
        }

        val transformedData = TransformEngine.transform(validationResult.acceptedRecords, job.transform)
        logger.info("Transformed rows: ${transformedData.size}")
        if (transformedData.isEmpty()) {
            logger.warn(" No records to load. Skipping load step.")
            return
        }

        LoadEngine.load(transformedData, job.load)
    }
}