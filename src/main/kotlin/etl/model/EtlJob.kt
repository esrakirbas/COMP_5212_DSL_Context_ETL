package etl.model

data class EtlJob(
    val sources: List<Source>,
    val schema: Schema,
    val transform: Transform,
    val load: Load
)
