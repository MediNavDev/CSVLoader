package com.scienaptic.csvloader.db

import java.nio.file.Path

import com.scienaptic.csvloader.{CsvParserConfig, Schema}

object SchemaBuilder2 {
  def createDDL(schema: Schema, dbType: DatasourceType): String = dbType match {
    case DatasourceType.MySQL => MySQLBuilder.createDDL(schema)
    case DatasourceType.PostgreSQL => ???
    case DatasourceType.H2DB => ???
  }

  def createInsertQry(schema: Schema, path: Path, config: CsvParserConfig, dbType: DatasourceType): String = dbType match {
    case DatasourceType.MySQL => MySQLBuilder.createInsertQry(schema, path, config)
    case DatasourceType.PostgreSQL => ???
    case DatasourceType.H2DB => ???
  }

}

