import java.nio.file.Paths
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import com.scienaptic.csvloader.FieldTypes.{CATEGORY, INTEGER, SHORT_INT}
import com.scienaptic.csvloader.db.MySQLBuilder
import com.scienaptic.csvloader.{CsvParserConfig, Field, Schema}
import org.apache.commons.lang3.StringEscapeUtils

val path = Paths.get("/Users/apple/Downloads/scienaptic.datasets/HACKER_EARTH_dataset/users.csv")
val config = CsvParserConfig.defaultConfig
val schema = Schema("dbName", "tableName", List(Field("user_id_1", CATEGORY), Field("user_id", INTEGER), Field("skills", CATEGORY), Field("solved_count", SHORT_INT), Field("attempts", SHORT_INT), Field("user_type", CATEGORY)))
val ignoreHeader = config.hasHeader

val anotherConfig = CsvParserConfig.defaultBuilder
  .hasHeader(false)
  .build()


"+" + StringEscapeUtils.escapeJava(config.lineSeparator)
MySQLBuilder.createInsertQry(schema, path, config)
MySQLBuilder.createInsertQry(schema, path, anotherConfig)


val time = "10:32:21"
//LocalDate.parse(time, DateTimeFormatter.ISO_LOCAL_TIME)
val fmt = DateTimeFormatter.ofPattern("HH:mm:ss")
LocalTime.parse(time, fmt)
