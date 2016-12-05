package com.scienaptic.csvloader.db

import java.util.Properties


case class JdbcDetails(url: String, user: String, pass: String) {
  lazy val resolveDriver: String = JdbcDetails.resolveDriver(url)
  lazy val dbType: DatasourceType = JdbcDetails.dbType(url)
  lazy val props: Properties = {
    val props = new Properties()
    props.put("user", user)
    props.put("password", pass)
    props.put("driver", resolveDriver)
    props
  }

}

object JdbcDetails {
  def resolveDriver(url: String): String = url match {
    case u if u.startsWith("jdbc:mysql:") => "com.mysql.jdbc.Driver"
    case u if u.startsWith("jdbc:postgresql:") => "org.postgresql.Driver"
    case u if u.startsWith("jdbc:h2:") => "org.h2.Driver"
    case u if u.startsWith("jdbc:monetdb:") => "nl.cwi.monetdb.jdbc.MonetDriver"
    case _ => sys.error("No valid driver resolved for url " + url)
  }

  def dbType(url: String): DatasourceType = {
    import DatasourceType._
    url match {
      case u if u.startsWith("jdbc:mysql:") => MySQL
      case u if u.startsWith("jdbc:postgresql:") => PostgreSQL
      case u if u.startsWith("jdbc:h2:") => H2DB
      case _ => Undefined
    }
  }
}
