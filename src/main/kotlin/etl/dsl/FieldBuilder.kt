package etl.dsl

import etl.model.*

@EtlDslMarker
class FieldBuilder(private val name: String) {
    val validationRules = mutableListOf<ValidationRule>()

    fun notEmpty() {
        validationRules.add(NotEmpty)
    }

    fun rejectIfInvalid() {
        validationRules.add(RejectIfInvalid)
    }

    fun isNumber() {
        validationRules.add(IsNumber)
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
