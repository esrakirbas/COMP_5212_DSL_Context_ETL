package etl.dsl

import etl.model.Clean
import etl.model.DataCleaningOperation

@EtlDslMarker
class CleanBuilder {
    val dataCleaningOperations = mutableListOf<DataCleaningOperation>()

    fun trim(field: String) {
        dataCleaningOperations.add(DataCleaningOperation.Trim(field))
    }
    fun toTitleCase(field: String) {
        dataCleaningOperations.add(DataCleaningOperation.ToTitleCase(field))
    }
    fun toLowerCase(field: String) {
        dataCleaningOperations.add(DataCleaningOperation.ToLowerCase(field))
    }

    fun defaultIfEmpty(field: String, defaultValue: String) {
        dataCleaningOperations.add(DataCleaningOperation.DefaultIfEmpty(field, defaultValue))
    }

    fun replaceWith(field: String, value: String, replaceWith: String) {
        dataCleaningOperations.add(DataCleaningOperation.ReplaceWith(field, value, replaceWith))
    }

    fun build(): Clean {
        return Clean(dataCleaningOperations)
    }
}