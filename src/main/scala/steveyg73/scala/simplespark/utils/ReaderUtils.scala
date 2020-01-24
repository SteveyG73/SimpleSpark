package com.worldremit.bigdata.scala.simplespark.utils

import scala.io.Source
import scala.util.Try
import java.text.SimpleDateFormat

import org.apache.spark.sql.types.{DataType, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}


object ReaderUtils {

  val sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

  def extractSchema(df: DataFrame): String = {
    df.schema.prettyJson

  }

  def loadSchemaFromFile(filename: String): Option[StructType] = {
    val rawSchema = Source.fromFile(filename).getLines.mkString
    Try(DataType.fromJson(rawSchema)).toOption.flatMap {
      case s: StructType => Some(s)
      case _ => None
    }
  }

  def readJsonFile(fileName: String, schema: Option[StructType])(implicit spark: SparkSession): DataFrame = {
    if (schema.isEmpty) {
      throw new RuntimeException("Schema is missing")
    }
    else {
      spark.read.schema(schema.get).json(fileName)
    }
  }

  def readDelimitedFile(fileName: String, delimiter: String = ",", header: Boolean = false)
                       (implicit spark: SparkSession): DataFrame = {
      spark.read.format("csv")
      .option("delimiter", value = delimiter)
      .option("header", value = header)
      .load(fileName)
  }

  def readParquetFile(fileName: String)(implicit spark: SparkSession): DataFrame = {
    spark.read.parquet(fileName)
  }

}
