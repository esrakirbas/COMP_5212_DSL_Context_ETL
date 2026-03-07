package etl.model

/** Field definition block (under schema) **/
data class Field(
    val name: String,
    val rules: List<ValidationRule>
)
