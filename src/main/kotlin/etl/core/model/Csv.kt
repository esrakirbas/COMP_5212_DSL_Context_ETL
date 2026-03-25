package etl.core.model

/** Csv definition block (under schema) **/
data class Csv(
    val fileName: String,
    val overwrite: Boolean
)
