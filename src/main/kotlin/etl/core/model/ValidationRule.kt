package etl.core.model

/**
 * Different types of validation rules under field block
 * This sealed interface defines these validation rules
 */
sealed interface ValidationRule

//stateless rules ar defined as singleton
object NotEmpty : ValidationRule {
    override fun toString() = "NotEmpty"
}
object IsNumber : ValidationRule {
    override fun toString() = "IsNumber"
}

//parametrized rules defined as data classes
data class Min(val value: Int) : ValidationRule
data class Max(val value: Int) : ValidationRule
data class MinLength(val length: Int) : ValidationRule
data class MaxLength(val length: Int) : ValidationRule