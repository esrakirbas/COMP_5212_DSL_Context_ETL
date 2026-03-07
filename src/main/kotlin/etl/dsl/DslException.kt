package etl.dsl

/**
 * custom exception for DSL errors
 */
class DslException(message: String) : IllegalStateException(message) {
}