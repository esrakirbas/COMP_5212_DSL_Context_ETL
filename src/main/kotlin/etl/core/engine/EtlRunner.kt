package etl.core.engine

import etl.core.model.EtlJob
import etl.core.model.InvalidPolicy
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
        logger.info("Records passed to projection(SchemaEngine): ${extractedData.size}")
        val projectedData = SchemaEngine.project(extractedData, job.schema)
        logger.info("Records passed to validation(SchemaEngine): ${projectedData.size}")
        val validationResult = SchemaEngine.validate(projectedData, job.schema)
        logger.info("Valid records : ${validationResult.validRecords.size}")
        logger.info("Invalid records: ${validationResult.invalidRecords.size}")
        validationResult.errors.forEach { e -> logger.error("Validation Error : ${e.field}: ${e.message}") }

        val recordsToProcess = when (job.invalidPolicy) {
            InvalidPolicy.REJECT -> validationResult.validRecords
            InvalidPolicy.KEEP -> validationResult.validRecords + validationResult.invalidRecords
        }
        logger.info("Invalid policy: ${job.invalidPolicy}")
        logger.info("Records passed to transform: ${recordsToProcess.size}")

        val transformedData = TransformEngine.transform(recordsToProcess, job.transform)
        logger.info("Transformed rows: ${transformedData.size}")
        if (transformedData.isEmpty()) {
            logger.warn(" No records to load. Skipping load step.")
            return
        }
        //load data to target CSV file
        LoadEngine.load(transformedData, job.load)

        //if there are rejected records, load them to a separate file to make them observable
        if (job.invalidPolicy == InvalidPolicy.REJECT && validationResult.invalidRecords.isNotEmpty()) {
            val rejectedPath = job.load.csv.fileName.replace(".csv", "_rejected.csv")
            logger.info("Writing rejected records to: $rejectedPath")
            LoadEngine.load(validationResult.invalidRecords, rejectedPath)
        }
    }
}