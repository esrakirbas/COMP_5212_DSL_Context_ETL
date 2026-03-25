package etl.core.dsl

import etl.core.model.*

/** Extract tag . Extract from multiple Sources csv. json and excel **/
@EtlDslMarker
class ExtractBuilder {
    private val sources = mutableListOf<Source>()

    private fun addSource(source: Source) {
        if (sources.contains(source)) {
            throw DslException("Duplicate Source : $source")
        }
        sources += source
    }

    fun csv(path: String) {
        checkFileType(path, "csv")
        addSource(CsvSource(path))
    }

    fun json(path: String) {
        checkFileType(path, "json")
        addSource(JsonSource(path))
    }

    fun excel(path: String) {
        checkFileType(path, "xlsx", "xls")
        addSource(ExcelSource(path))
    }

    fun build(): Extract {
        if (sources.isEmpty()) {
            throw DslException("At least one source is required in extract part.")
        }
        return Extract(sources.toList())
    }

    private fun checkFileType(path: String, vararg allowed: String) {
        val ext = path.substringAfterLast('.', "").lowercase()
        if (ext !in allowed) {
            throw DslException("Invalid file type for '$path'. Expected: ${allowed.joinToString(", ")}")
        }
    }
}