package etl.model

/**
 * Different types of data cleaning operations under clean block
 * This sealed interface defines these cleaning operations
 */
sealed interface DataCleaningOperation {
    //parametrized operations defined as data classes
    data class Trim(val field: String) : DataCleaningOperation
    data class ToTitleCase(val field: String) : DataCleaningOperation
    data class ToLowerCase(val field: String) : DataCleaningOperation
    data class DefaultIfEmpty(val field: String, val defaultValue: String) : DataCleaningOperation
    data class ReplaceWith(val field: String, val value: String, val replaceWith: String) : DataCleaningOperation
}