package etl.core.model

/**
 * Different types of validation rules under field block
 * This sealed interface defines these validation rules
 */
sealed interface ValidationRule

//stateless rules ar defined as singleton
object NotEmpty : ValidationRule
object RejectIfInvalid : ValidationRule
object IsNumber : ValidationRule

//parametrized rules defined as data classes
data class MinLength(val length: Int) : ValidationRule
data class MaxLength(val length: Int) : ValidationRule