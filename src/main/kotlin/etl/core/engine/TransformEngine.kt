package etl.core.engine

import etl.core.model.*
import etl.util.Logger

object TransformEngine {

    private val logger = Logger.forClass(TransformEngine::class)

    fun transform(records: List<Record>, transform: Transform?): List<Record> {
        if (transform == null) return records.map { it.toMutableMap() }

        logger.info("Starting transform phase. Input records: ${records.size}")

        var result = records.map { it.toMutableMap() }

        transform.clean?.let { clean ->
            result = applyClean(result, clean)
            logger.info("Cleaning completed for ${records.size} records.")
        }

        transform.filter?.let { filter ->
            result = applyFilter(result, filter)
        }

        return result
    }

    private fun applyClean(records: List<Record>, clean: Clean): List<Record> {
        logger.info("Applying ${clean.operations.size} cleaning operation(s). ")
        return records.map { record ->
            clean.operations.fold(record.toMutableMap()) { acc, operation ->
                clean(acc, operation)
            }
        }
    }

    private fun clean(record: Record, operation: DataCleaningOperation): Record {
        return record.toMutableMap().apply {
            when (operation) {
                is DataCleaningOperation.DefaultIfEmpty -> {
                    val value = this[operation.field]
                    if (value.isNullOrBlank()) {
                        this[operation.field] = operation.defaultValue
                    }
                }

                is DataCleaningOperation.Replace -> {
                    val value = this[operation.field]
                    if (value != null) {
                        this[operation.field] =
                            value.replace(operation.value, operation.replace)
                    }
                }

                is DataCleaningOperation.Trim -> {
                    val value = this[operation.field]
                    if (value != null) {
                        this[operation.field] = value.trim()
                    }
                }

                is DataCleaningOperation.ToLowerCase -> {
                    val value = this[operation.field]
                    if (value != null) {
                        this[operation.field] = value.lowercase()
                    }
                }

                is DataCleaningOperation.ToTitleCase -> {
                    val value = this[operation.field]
                    if (value != null) {
                        this[operation.field] = value
                            .lowercase()
                            .split(" ")
                            .map { w ->
                                w.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase() else it.toString()
                                }
                            }.joinToString(" ")
                    }
                }
            }
        }
    }

    private fun applyFilter(records: List<Record>, filter: Filter): List<Record> {
        val before = records.size

        val filtered = records.filter { record ->
            filter.conditions.all { condition ->
                matches(record, condition)
            }
        }

        val after = filtered.size

        logger.info("Filter applied. Records before: $before, after: $after, removed: ${before - after}")

        return filtered
    }

    private fun matches(record: Record, condition: DataFilteringCondition): Boolean {
        return when (condition) {
            is DataFilteringCondition.Equals ->
                record[condition.field] == condition.value

            is DataFilteringCondition.NotEquals ->
                record[condition.field] != condition.value

            is DataFilteringCondition.Empty ->
                record[condition.field].isNullOrBlank()

            is DataFilteringCondition.NotEmpty ->
                !record[condition.field].isNullOrBlank()

            is DataFilteringCondition.Contains ->
                record[condition.field]?.contains(condition.value) == true

            is DataFilteringCondition.GreaterThan -> {
                val left = record[condition.field]?.toDoubleOrNull()
                val right = condition.value.toDoubleOrNull()
                left != null && right != null && left > right
            }

            is DataFilteringCondition.LessThan -> {
                val left = record[condition.field]?.toDoubleOrNull()
                val right = condition.value.toDoubleOrNull()
                left != null && right != null && left < right
            }
        }
    }
}