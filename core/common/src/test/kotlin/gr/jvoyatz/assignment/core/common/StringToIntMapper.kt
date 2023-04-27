package gr.jvoyatz.assignment.core.common

import gr.jvoyatz.assignment.core.common.utils.Mapper

object StringToIntDtoMapper : Mapper<String, Int> {
    override fun String.to() = this.toInt()

    override fun Int.from()= this.toString()
}