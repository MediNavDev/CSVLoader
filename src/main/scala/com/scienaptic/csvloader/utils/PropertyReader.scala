package com.scienaptic.csvloader.utils

import java.util
import java.util.Map.Entry
import javax.sql.DataSource

import com.scienaptic.csvloader.CsvParserConfig
import com.scienaptic.csvloader.db.JdbcDetails
import com.typesafe.config.{Config, ConfigFactory, ConfigRenderOptions, ConfigValue}
import com.typesafe.scalalogging.LazyLogging
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.springframework.jdbc.core.JdbcTemplate

import scala.collection.JavaConversions

object PropertyReader extends LazyLogging {
  private lazy val config = {
    val cfg: Config = ConfigFactory.load()
    val renderOpts = ConfigRenderOptions.defaults()
      .setComments(false)
      .setFormatted(true)
      .setJson(false)
      .setOriginComments(true)

    logger.info("Config loaded: {}", cfg.root().render(renderOpts))
    cfg
  }

  def parserConfigs: List[CsvParserConfig] = {
    val jList = config.getConfigList("parserConfigs")
    JavaConversions.asScalaBuffer(jList).toList.map(CsvParserConfig.fromHOCON)
  }

  class ConfigContainer(val filePath: String, val config: CsvParserConfig, val tableName: String)

  def filesAndParsers: List[ConfigContainer] = ???

  def jdbcDetails: JdbcDetails = {
    val cfg: Config = config.getConfig("jdbcDetails")
    JdbcDetails(cfg.getString("url"), cfg.getString("user"), cfg.getString("pass"))
  }

  def createTemplate(targetDetails: JdbcDetails): JdbcTemplate = {
    val dataSource = createDataSource(targetDetails)
    val template: JdbcTemplate = new JdbcTemplate(dataSource)
    template
  }

  def createDataSource(targetDetails: JdbcDetails): DataSource = {
    val hikariConfig: HikariConfig = new HikariConfig()
    hikariConfig.setJdbcUrl(targetDetails.url)
    hikariConfig.setUsername(targetDetails.user)
    hikariConfig.setPassword(targetDetails.pass)
    if (config.hasPath("datasourceParams")) {
      val dsParams: Config = config.getConfig("datasourceParams")
      val iterator: util.Iterator[Entry[String, ConfigValue]] = dsParams.entrySet().iterator()
      while (iterator.hasNext) {
        val next: Entry[String, ConfigValue] = iterator.next()
        logger.info(s"Adding ${next.getKey} ==> ${next.getValue.unwrapped()}")
        hikariConfig.addDataSourceProperty(next.getKey, next.getValue.unwrapped())
      }
    }

    new HikariDataSource(hikariConfig)
  }

  def schemaName: String = config.getString("schemaName")
  def sampleThreshold: Float = config.getString("sampleThreshold").toFloat
}
