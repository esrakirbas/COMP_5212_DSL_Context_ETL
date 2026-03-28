package etl.core.model

data class ValidationResult (
    val validRecords: List<Record>,
    val invalidRecords: List<Record>,
    val errors: List<RecordError>
)