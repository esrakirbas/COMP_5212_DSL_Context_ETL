package etl.core.dsl

import etl.core.model.*

@EtlDslMarker
class FieldBuilder(private val name: String) {
    val validationRules = mutableListOf<ValidationRule>()

    fun notEmpty() {
        validationRules.add(NotEmpty)
    }

    fun isNumber() {
        validationRules.add(IsNumber)
    }

    fun min(value: Int) {
        validationRules.add(Min(value))
    }

    fun max(value: Int) {
        validationRules.add(Max(value))
    }

    fun minLength(length: Int) {
        validationRules.add(MinLength(length))
    }

    fun maxLength(length: Int) {
        validationRules.add(MaxLength(length))
    }

    fun build(): Field {
        return Field(name, validationRules)
    }
}
