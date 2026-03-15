package etl.dsl

import etl.model.DataFilteringCondition
import etl.model.Filter

class FilterBuilder {
    val dataFilteringConditions = mutableListOf<DataFilteringCondition>()

    fun equals(field: String, value: String) {
        dataFilteringConditions.add(DataFilteringCondition.Equals(field, value))
    }

    fun notEquals(field: String, value: String) {
        dataFilteringConditions.add(DataFilteringCondition.NotEquals(field, value))
    }

    fun empty(field: String) {
        dataFilteringConditions.add(DataFilteringCondition.Empty(field))
    }

    fun notEmpty(field: String) {
        dataFilteringConditions.add(DataFilteringCondition.NotEmpty(field))
    }

    fun greaterThan(field: String, value: String) {
        dataFilteringConditions.add(DataFilteringCondition.GreaterThan(field, value))
    }

    fun lessThan(field: String, value: String) {
        dataFilteringConditions.add(DataFilteringCondition.LessThan(field, value))
    }

    fun contains(field: String, value: String) {
        dataFilteringConditions.add(DataFilteringCondition.Contains(field, value))
    }

    fun build(): Filter {
        return Filter(dataFilteringConditions)
    }
}