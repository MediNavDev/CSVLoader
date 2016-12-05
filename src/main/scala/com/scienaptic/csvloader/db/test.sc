import java.nio.file.Paths

import org.apache.commons.lang3.StringEscapeUtils
import org.kogu.csv.FieldTypes.{CATEGORY, INTEGER, SHORT_INT}
import org.kogu.csv.db.MySQLBuilder
import org.kogu.csv.{CsvParserConfig, Field, Schema}


val path = Paths.get("/Users/apple/Downloads/scienaptic.datasets/HACKER_EARTH_dataset/users.csv")
val config = CsvParserConfig.defaultConfig
val schema = Schema("dbName", "tableName", List(Field("user_id_1", CATEGORY), Field("user_id", INTEGER), Field("skills", CATEGORY), Field("solved_count", SHORT_INT), Field("attempts", SHORT_INT), Field("user_type", CATEGORY)))
val ignoreHeader = config.hasHeader

val anotherConfig = CsvParserConfig.defaultBuilder
  .setHasHeader(false)
  .build()


"+" + StringEscapeUtils.escapeJava(config.lineSeparator)
MySQLBuilder.createInsertQry(schema, path, config)
MySQLBuilder.createInsertQry(schema, path, anotherConfig)