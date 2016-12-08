package com.scienaptic.csvloader.utils

import com.scienaptic.csvloader.CsvParserConfig
import com.typesafe.config.Config

class ConfigContainer(val filePath: String,
                      val config: CsvParserConfig,
                      val tableName: String)

object ConfigContainer {
  def fromHocon(cfg: Config): ConfigContainer = {
    val path = cfg.getString("filePath")
    val table = cfg.getString("tableName")
    val parserConfig = CsvParserConfig.fromHocon(cfg.getConfig("parserConfig"))
    new ConfigContainer(path, parserConfig, table)
  }
}