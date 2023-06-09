package gr.jvoyatz.assignment.core.common.utils


interface Mapper<From, To> {
    fun From.to(): To
    fun To.from(): From
}

inline fun <I, O> List<I>.mapList(mapSingle: (I) -> O): List<O> {
    return this.map { mapSingle(it) }
}
