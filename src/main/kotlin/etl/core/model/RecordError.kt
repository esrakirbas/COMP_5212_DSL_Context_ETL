package etl.core.model

data class RecordError(
    val rowIndex: Int,
    val field: String,
    val message: String
)