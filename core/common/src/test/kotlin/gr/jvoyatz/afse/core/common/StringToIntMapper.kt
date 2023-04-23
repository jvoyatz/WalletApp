package gr.jvoyatz.afse.core.common

object StringToIntDtoMapper : Mapper<String, Int> {
    override fun String.to() = this.toInt()

    override fun Int.from()= this.toString()
}