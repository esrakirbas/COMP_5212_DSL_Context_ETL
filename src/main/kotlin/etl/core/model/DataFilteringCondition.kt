package etl.core.model

/**
 * Different types of data filtering conditions under filter block
 * This sealed interface defines these filtering conditions
 */
sealed interface DataFilteringCondition {
    data class Equals(val field: String, val value: String) : DataFilteringCondition
    data class NotEquals(val field: String, val value: String) : DataFilteringCondition
    data class NotEmpty(val field: String) : DataFilteringCondition
    data class Empty(val field: String) : DataFilteringCondition
    data class GreaterThan(val field: String, val value: String) : DataFilteringCondition
    data class LessThan(val field: String, val value: String) : DataFilteringCondition
    data class Contains(val field: String, val value: String) : DataFilteringCondition
}