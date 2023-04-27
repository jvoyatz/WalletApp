package gr.jvoyatz.afse.core.common

import gr.jvoyatz.afse.core.common.utils.Mapper

object StringToIntDtoMapper : Mapper<String, Int> {
    override fun String.to() = this.toInt()

    override fun Int.from()= this.toString()
}