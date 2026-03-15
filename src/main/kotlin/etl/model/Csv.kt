package etl.model

/** Csv definition block (under schema) **/
data class Csv(
    val fileName: String,
    val overwrite: Boolean
)
