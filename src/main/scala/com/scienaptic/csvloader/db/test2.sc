import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val fmt = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
val format = DateTimeFormatter.ofPattern(fmt)
//LocalDateTime.parse("1997-07-16T19:20:30.45+01:00", format)

LocalDateTime.parse("1997-07-16T19:20:30.45+01:00", DateTimeFormatter.ISO_DATE_TIME)
LocalDateTime.parse("2016-05-18T16:00:00Z", DateTimeFormatter.ISO_DATE_TIME)
