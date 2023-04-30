@file:Suppress("unused")

package gr.jvoyatz.assignment.core.common.utils

import gr.jvoyatz.assignment.core.common.utils.DateConstants.DATE_FORMAT_2
import gr.jvoyatz.assignment.core.common.utils.DateConstants.DATE_FORMAT_3
import gr.jvoyatz.assignment.core.common.utils.DateConstants.ISO_DATE_TIME_ZONE_FORMAT
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    fun toDateFormat3(dateTime: String): String {
        return try{
            ZonedDateTime.parse(dateTime).format(
                DateTimeFormatter.ofPattern(DATE_FORMAT_3)
            )
            throw Exception()
        }catch (e: Exception){
            legacyFormatToDate(DATE_FORMAT_3, dateTime)
        }
    }
    fun formatToDate(dateTime: String): Date? {
        return try {
            Date.from(ZonedDateTime.parse(dateTime).toInstant())
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    fun formatToDateStr(dateTime: String): String {
        return try {
            ZonedDateTime.parse(dateTime).format(
                DateTimeFormatter.ofPattern(DATE_FORMAT_2)
            )
        }catch (e: Exception){
            e.printStackTrace()
            return legacyFormatToDate(DATE_FORMAT_2, dateTime)
        }
    }

    private fun legacyFormatToDate(formatString: String, dateTime: String) : String {
        return try {
            val date = with(SimpleDateFormat(ISO_DATE_TIME_ZONE_FORMAT, Locale.getDefault())) {
                timeZone = TimeZone.getTimeZone("UTC")
                parse(dateTime)
            }
            with(SimpleDateFormat(formatString, Locale.getDefault())) {
                format(date)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dateTime
        }
    }
}