package etl.dsl

import etl.model.Clean
import etl.model.Filter
import etl.model.Transform

@EtlDslMarker
class TransformBuilder {
    private var clean: Clean? =
        null //allow temporarily incomplete state while constructing. After build() it will be immutable (see EtlJob.kt)
    private var filter: Filter? =
        null //allow temporarily incomplete state while constructing. After build() it will be immutable (see EtlJob.kt)

    fun clean(block: CleanBuilder.() -> Unit) {
        val builder = CleanBuilder()
        if (clean != null) throw DslException("Duplicate clean specification is not allowed.")
        builder.block()
        clean = builder.build()
    }

    fun filter(block: FilterBuilder.() -> Unit) {
        val builder = FilterBuilder()
        if (filter != null) throw DslException("Duplicate filter specification is not allowed.")
        builder.block()
        filter = builder.build()
    }

    fun build(): Transform {
        return Transform(
            clean = clean,
            filter = filter
        )
    }
}