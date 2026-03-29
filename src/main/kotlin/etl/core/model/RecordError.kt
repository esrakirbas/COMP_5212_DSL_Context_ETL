package etl.core.model

data class RecordError(
    val field: String,
    val message: String
)