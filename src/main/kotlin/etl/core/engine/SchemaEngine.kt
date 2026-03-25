package etl.core.engine

import etl.core.model.*

object SchemaEngine {
    fun validate(records: List<Record>, schema: Schema, logger: ((String) -> Unit)? = null): ValidationResult {
        val fieldErrors = mutableMapOf<String, Int>()
        var invalidCount = 0
        var rejectedCount = 0
        val validRecords = mutableListOf<Record>()
        for (record in records) {
            val validationResult = validateRecord(record, schema, logger, fieldErrors)
            when (validationResult) {
                RecordStatus.VALID -> {
                    validRecords += record
                }

                RecordStatus.REJECTED -> {
                    rejectedCount += 1
                    invalidCount += 1
                }

                RecordStatus.INVALID_KEPT -> {
                    invalidCount += 1
                    validRecords += record
                }
            }
        }
        return ValidationResult(validRecords, invalidCount, rejectedCount, fieldErrors)
    }

    private fun validateRecord(
        record: Record,
        schema: Schema,
        logger: ((String) -> Unit)?,
        fieldErrors: MutableMap<String, Int>
    ): RecordStatus {
        var hasValidationError = false
        for (field in schema.fields) {
            val value = record[field.name]
            val shouldReject = field.rules.any { it is RejectIfInvalid }
            val failed = field.rules.filterNot { it is RejectIfInvalid }
                .any { rule ->
                    val valid = applyRule(value, rule)
                    if (!valid) {
                        hasValidationError = true
                        logger?.invoke("Invalid value for field ${field.name}: ${if (value.isNullOrEmpty()) "EMPTY" else value}")
                        fieldErrors[field.name] =
                            fieldErrors.getOrDefault(field.name, 0) + 1
                    }
                    !valid
                }
            if (failed && shouldReject) {
                return RecordStatus.REJECTED
            }
        }
        return if (hasValidationError) {
            RecordStatus.INVALID_KEPT
        } else {
            RecordStatus.VALID
        }
    }

    private fun applyRule(value: String?, rule: ValidationRule): Boolean {
        return when (rule) {
            is NotEmpty -> !value.isNullOrBlank()
            is IsNumber -> value?.toDoubleOrNull() != null
            is MaxLength -> (value?.length ?: 0) <= rule.length
            is MinLength -> (value?.length ?: 0) >= rule.length
            is RejectIfInvalid -> true // handled outside
        }
    }
}