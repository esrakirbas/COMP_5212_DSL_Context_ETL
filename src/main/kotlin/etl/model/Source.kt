package etl.model

/** Sources can come from multiple file types
 * this sealed interface defines these file types **/
sealed interface Source

/**
 * Actual data classes containing the data in the files
 */
data class CsvSource(val path: String) : Source
data class JsonSource(val path: String) : Source
data class ExcelSource(val path: String) : Source
