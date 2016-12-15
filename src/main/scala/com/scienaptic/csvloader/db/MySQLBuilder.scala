package com.scienaptic.csvloader.db

import java.nio.file.Path

import com.scienaptic.csvloader.FieldTypes._
import com.scienaptic.csvloader.utils.TypeUtil
import com.scienaptic.csvloader.{CsvParserConfig, Field, FieldType, Schema}
import org.apache.commons.lang3.StringEscapeUtils

import scala.collection.JavaConversions

object MySQLBuilder {
  // map of javaFormats to mySQL Formats
  val mysqlDateFormats: String => String = Map(
    // date
    "yyyy-MM-dd" -> "%Y-%m-%d",
    "dd/MM/yyyy" -> "%d/%m/%Y",
    "dd-MMM-yyyy" -> "%d-%M-%y",
    "dd-MM-yyyy" -> "%d-%m-%Y",

    // date time
    "yyyy-MM-dd HH:mm:ss" -> "%Y-%m-%d %H:%i:%s",
    "yyyy-MM-dd'T'HH:mm:ss" -> "%Y-%m-%dT%H:%i:%s"
  )

  val mysqlBooleanFormats = {
    val seq = JavaConversions.asScalaBuffer(TypeUtil.TRUE_STRINGS_FOR_DETECTION).toIndexedSeq
    seq.map(s => "'" + s + "'" ).mkString(",")
  }

  val fieldExpr: FieldType => String = {
    case BOOLEAN => "TINYINT(1)"
    case CATEGORY => "VARCHAR(255)"
    case FLOAT => "DECIMAL(10, 5)"
    case SHORT_INT => "MEDIUMINT"
    case INTEGER => "INT"
    case LONG_INT => "BIGINT"
    case LOCAL_DATE(dateFormat) => "DATE"
    case LOCAL_DATE_TIME(dateFormat) => "DATETIME"
    case LOCAL_TIME(dateFormat) => "TIME"
  }

  def createDDL(schema: Schema): String = {
    def rowDDL(f: Field): String = s"${f.name.toUpperCase()} ${fieldExpr(f.fieldType)}"

    val ddl =
      s"""
         |create table if not exists ${schema.table} (
         |  ${schema.fields.map(rowDDL).mkString(", ")}
         |)  ENGINE=InnoDB CHARSET=utf8;
      """.stripMargin

    ddl
  }

  def createInsertQry(schema: Schema, path: Path, config: CsvParserConfig): String = {
    //    LOAD DATA INFILE 'file.txt'
    //    INTO TABLE t1
    //    FIELDS TERMINATED BY ','
    //    (column1, @var1, column3, ...)
    //    SET column2 = STR_TO_DATE(@var1,'%Y%m%d'), colName = func(...)

    val ignoreHeader = if(config.hasHeader) "IGNORE 1 LINES" else ""

    val buffer = new scala.collection.mutable.ListBuffer[String]()

    def stringToDate(name: String, fmt: String, idx: Int): String = {
      buffer += s"$name = STR_TO_DATE(@var$idx, '${mysqlDateFormats(fmt)}')"
      s"@var$idx"
    }

    def stringToBoolean(name: String, idx: Int): String = {
      buffer += s"$name = (@var$idx IN ($mysqlBooleanFormats))"
      s"@var$idx"
    }

    val columnNames = schema.fields.zipWithIndex.map {
      case (Field(name, LOCAL_DATE(fmt)), idx) => stringToDate(name, fmt, idx)

      case (Field(name, LOCAL_DATE_TIME(fmt)), idx) => stringToDate(name, fmt, idx)

      case (Field(name, LOCAL_TIME(fmt)), idx) => stringToDate(name, fmt, idx)

      case (Field(name, BOOLEAN), idx) => stringToBoolean(name, idx)

      case (Field(name, _), _) => name

    }.mkString("(", ",", ")")

    val setters = if(buffer.nonEmpty) "SET " + buffer.mkString(", ") else ""

    val qry =
      s"""
         |LOAD DATA LOCAL INFILE '${path.toAbsolutePath.toString}'
         | INTO TABLE ${schema.table}
         | FIELDS TERMINATED BY '${config.fieldSeparator}'
         | ENCLOSED BY '${config.fieldDelimiter}'
         | LINES TERMINATED BY '${StringEscapeUtils.escapeJava(config.lineSeparator)}'
         | $ignoreHeader
         | $columnNames
         | $setters
      """.stripMargin
    qry
  }
}

