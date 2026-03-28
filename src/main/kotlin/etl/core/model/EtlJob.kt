package etl.core.model

data class EtlJob(
    val extract: Extract,
    val schema: Schema,
    val invalidPolicy: InvalidPolicy,
    val transform: Transform?,
    val load: Load,
    val log: Log?
)
