package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object DateTimeConverter {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.LL.yyyy HH:mm:ss")
    val dayOfWeekShort: DateTimeFormatter = DateTimeFormatter.ofPattern("EE")
    var MMMMyyyy: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    fun dateTimeToString(dateTime: LocalDateTime): String {
        return dateTime.format(formatter)
    }

    fun stringToDateTime(formattedString: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(formattedString, formatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }

}