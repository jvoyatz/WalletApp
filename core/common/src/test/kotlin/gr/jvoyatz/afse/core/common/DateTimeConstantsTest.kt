package gr.jvoyatz.afse.core.common

import io.mockk.InternalPlatformDsl.toStr
import org.junit.Test
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


class DateTimeConstantsTest {

    @Test
    fun `iso date time string`(){
        //given
        val dateTime = "2015-12-03T10:15:30Z"

        //when
        val date = SimpleDateFormat(DateTimeConstants.ISO_DATE_TIME_ZONE_FORMAT).parse(dateTime)
        println(date)
//        val date = ZonedDateTime.parse(dateTime)
//        println("date ${date.toStr()}")
//
//        val javaDate = Date.from(date.toInstant())
//        println(javaDate)
//
//        val instantDate = Instant.parse(dateTime)
//        println("$instantDate")
//        val str = date.format(DateTimeFormatter.ofPattern(DateTimeConstants.DATE_FORMAT))
//        println(str)

        println(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(dateTime));
        println(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX").parse(dateTime));
        println(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(dateTime));
    }
}