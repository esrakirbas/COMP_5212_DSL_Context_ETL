package etl.core.model

data class ValidationResult (
    val acceptedRecords: List<Record>,
    val invalidCount: Int,
    val rejectedCount: Int,
    val fieldErrors: Map<String, Int>
)