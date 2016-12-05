import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder, DateTimeParseException}

private val dtf1: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
private val dtf2: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
private val dtf3: DateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
private val dtf4: DateTimeFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy")
private val dtf5: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val dtf6: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
private val dtf7: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy")
private val dtf8: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
private val dtf9: DateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
private val dtf10: DateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yy")
private val dtf11: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM/dd/yyyy")
private val dtf12: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM-dd-yyyy")
private val dtf13: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM/dd/yy")
private val dtf14: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM-dd-yy")
private val dtf15: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM/dd/yyyy")
private val dtf16: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM/d/yyyy")
private val dtf17: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM-dd-yy")
private val dtf18: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
private val dtf19: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

val dateFormatter = new DateTimeFormatterBuilder()
  .appendOptional(dtf1).appendOptional(dtf2).appendOptional(dtf2).appendOptional(dtf3).appendOptional(dtf4).appendOptional(dtf5).appendOptional(dtf6).appendOptional(dtf7).appendOptional(dtf8).appendOptional(dtf9).appendOptional(dtf10).appendOptional(dtf11).appendOptional(dtf12).appendOptional(dtf13).appendOptional(dtf14).appendOptional(dtf15).appendOptional(dtf16).appendOptional(dtf17).appendOptional(dtf18).appendOptional(dtf19).toFormatter

val localDate: LocalDate = LocalDate.parse("20160131", dateFormatter)



private val fmt1 = "yyyyMMdd"
private val fmt2 = "MM/dd/yyyy"
private val fmt3 = "MM-dd-yyyy"
private val fmt4 = "MM.dd.yyyy"
private val fmt5 = "yyyy-MM-dd"
private val fmt6 = "yyyy/MM/dd"
private val fmt7 = "dd/MMM/yyyy"
private val fmt8 = "dd-MMM-yyyy"
private val fmt9 = "M/d/yyyy"
private val fmt10 = "M/d/yy"
private val fmt11 = "MMM/dd/yyyy"
private val fmt12 = "MMM-dd-yyyy"
private val fmt13 = "MMM/dd/yy"
private val fmt14 = "MMM-dd-yy"
private val fmt15 = "MMM/dd/yyyy"
private val fmt16 = "MMM/d/yyyy"
private val fmt17 = "MMM-dd-yy"
private val fmt18 = "MMM dd, yyyy"
private val fmt19 = "MMM d, yyyy"

val formats = List(fmt1, fmt2, fmt3, fmt4, fmt5, fmt6, fmt7, fmt8, fmt9, fmt10, fmt11, fmt12, fmt13, fmt14, fmt15, fmt16, fmt17, fmt18, fmt19)

val xs: List[(String, DateTimeFormatter)] = formats.map(fmt => (fmt, DateTimeFormatter.ofPattern(fmt)))

def parseDateFormat(probablyDate: String): Option[String] = {
  xs.find {
    case (s, formatter) =>
      try {
        LocalDate.parse(probablyDate, formatter)
        true
      } catch {
        case e: DateTimeParseException => false
      }
  }.map(_._1)
}

parseDateFormat("2016/01/31")