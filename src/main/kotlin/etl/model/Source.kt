package etl.model

sealed interface Source

data class CsvSource(val path: String) : Source
data class JsonSource(val path: String) : Source
data class ExcelSource(val path: String) : Source
