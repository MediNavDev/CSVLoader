package com.scienaptic.csvloader.main

import java.nio.file.Paths
import java.util

import com.scienaptic.csvloader._
import com.scienaptic.csvloader.db.{JdbcDetails, SchemaBuilder2}
import com.scienaptic.csvloader.utils.{ConfigContainer, PropertyReader, Stopwatch}
import com.typesafe.scalalogging.LazyLogging
import org.springframework.jdbc.core.JdbcTemplate

import scala.collection.JavaConversions

object StandaloneRunner extends LazyLogging {
  def main(args: Array[String]): Unit = {
    logger.info("Looking up parser configurations .... ")

    val targetDetails: JdbcDetails = PropertyReader.jdbcDetails
    val jdbcTemplate: JdbcTemplate = PropertyReader.createTemplate(targetDetails)
    val schemaName = PropertyReader.schemaName
    val threshold = PropertyReader.sampleThreshold

    val configContainers: List[ConfigContainer] = PropertyReader.filesAndParsers
    logger.info("Configs obtained: {}", configContainers.mkString("\n"))

    configContainers.foreach(ioAction(schemaName, targetDetails, threshold, _, jdbcTemplate))
  }

  def ioAction(schemaName: String,
               details: JdbcDetails,
               threshold: Float,
               container: ConfigContainer,
               jdbcTemplate: JdbcTemplate): (String, Int) = {
    val path = Paths.get(container.filePath)
    val sample: util.List[Column] = Reader.sample(path, container.config, threshold)
    val fields: util.List[Field] = TypeInferer.inferFieldTypes(sample)
    val schema: Schema = Schema(schemaName, container.tableName, JavaConversions.asScalaBuffer(fields).toList)
    val ddl = SchemaBuilder2.createDDL(schema, details.dbType)

    logger.info("Executing ddl: {}", ddl)
    jdbcTemplate.execute(ddl)
    logger.info("Schema created")

    val insertQry = SchemaBuilder2.createInsertQry(schema, path, container.config, details.dbType)
    val count = insertData(jdbcTemplate, insertQry)
    (container.filePath, count)
  }

  def insertData(jdbcTemplate: JdbcTemplate, qry: String): Int = {
    logger.info("Insert query: [ {} ]", qry)
    val result = Stopwatch.time(jdbcTemplate.update(qry))
    logger.info(s"rows inserted: ${result._1}, time taken: ${result._2.toString}")
    result._1
  }

}
