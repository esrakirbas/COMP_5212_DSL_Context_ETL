package etl.core.dsl

import etl.core.model.Clean
import etl.core.model.DataCleaningOperation

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

    fun replace(field: String, value: String, replace: String) {
        dataCleaningOperations.add(DataCleaningOperation.Replace(field, value, replace))
    }

    fun build(): Clean {
        return Clean(dataCleaningOperations)
    }
}