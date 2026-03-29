package etl.core.engine

import etl.core.model.*

object SchemaEngine {

    //private val logger = Logger.forClass(SchemaEngine::class)

    fun validate(records: List<Record>, schema: Schema): ValidationResult {
        val errors = mutableListOf<RecordError>()
        val validRecords = mutableListOf<Record>()
        val invalidRecords = mutableListOf<Record>()
        records.forEach { record ->
            val validationResult = validateRecord(record, schema)
            when (validationResult.first) {
                RecordStatus.VALID -> {
                    validRecords += record
                }

                RecordStatus.INVALID -> {
                    invalidRecords += record
                    errors.addAll(validationResult.second)
                }
            }
        }
        return ValidationResult(validRecords, invalidRecords, errors)
    }

    private fun validateRecord(
        record: Record,
        schema: Schema
    ): Pair<RecordStatus, List<RecordError>> {
        var hasValidationError = false
        val recordErrors = mutableListOf<RecordError>()
        for (field in schema.fields) {
            val value = record[field.name]
            field.rules.any { rule ->
                val valid = applyRule(value, rule)
                if (!valid) {
                    hasValidationError = true
                    recordErrors.add(
                        RecordError(
                            field = field.name,
                            message = "Invalid value for field ${field.name}: ${if (value.isNullOrEmpty()) "EMPTY" else value} " +
                                    "rule: $rule (source : ${record["_source"]} , record : ${record["_recIndex"]})"
                        )
                    )
                }
                !valid
            }
        }
        return Pair(
            if (hasValidationError) RecordStatus.INVALID else RecordStatus.VALID,
            recordErrors
        )
    }

    private fun applyRule(value: String?, rule: ValidationRule): Boolean {
        return when (rule) {
            is NotEmpty -> !value.isNullOrBlank()
            is IsNumber -> value?.toDoubleOrNull() != null
            is MaxLength -> (value?.length ?: 0) <= rule.length
            is MinLength -> (value?.length ?: 0) >= rule.length
            is Max -> {
                val intValue = value?.toIntOrNull()
                intValue != null && intValue <= rule.value
            }

            is Min -> {
                val intValue = value?.toIntOrNull()
                intValue != null && intValue >= rule.value
            }
        }
    }

    fun project(records: List<Record>, schema: Schema): List<Record> {
        val allowedFields = schema.fields.map { it.name }.toSet()

        return records.map { record ->
            record
                .filterKeys { it in allowedFields || it.startsWith("_") }
                .toMutableMap()
        }
    }
}