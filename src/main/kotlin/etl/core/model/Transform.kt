package etl.core.model

data class Transform(
    val clean: Clean?,
    val filter: Filter?
)
