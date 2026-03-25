package etl.core.dsl

import etl.core.model.Csv

@EtlDslMarker
class CsvBuilder(private val fileName: String) {
    var overwrite: Boolean? = false

    fun overwrite() {
        overwrite = true
    }

    fun build(): Csv {
        return Csv(fileName, overwrite == true)
    }
}
