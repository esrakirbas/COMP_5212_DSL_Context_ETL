package etl.core.model

data class EtlJob(
    val extract: Extract,
    val schema: Schema,
    val transform: Transform?,
    val load: Load,
    val log: Log?
)
