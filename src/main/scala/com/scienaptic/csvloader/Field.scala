package com.scienaptic.csvloader

sealed trait FieldType

object FieldTypes {
  case object BOOLEAN extends FieldType
  case object CATEGORY extends FieldType
  case object FLOAT extends FieldType
  case object SHORT_INT extends FieldType
  case object INTEGER extends FieldType
  case object LONG_INT extends FieldType
  case class LOCAL_DATE(dateFormat: String) extends FieldType
  case class LOCAL_DATE_TIME(dateFormat: String) extends FieldType
  case class LOCAL_TIME(dateFormat: String) extends FieldType
}

case class Field(name: String, fieldType: FieldType)

case class Schema(db: String, table: String, fields: List[Field])
