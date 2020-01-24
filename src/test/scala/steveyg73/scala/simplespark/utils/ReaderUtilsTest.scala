package com.worldremit.bigdata.scala.simplespark.utils

import com.worldremit.bigdata.scala.simplespark.SparkSupport

class ReaderUtilsTest extends SparkSupport {

  behavior of "Utils"

  it should "readFromDelimitedFile - without a header" in {
    val delimitedFile: String = "src/test/resources/mock_data/delimited_file.csv"
    val expectedRows: Int = 11
    val actualRows: Int = ReaderUtils.readDelimitedFile(delimitedFile, header = false).count().toInt
    actualRows shouldEqual expectedRows
  }

  it should "readFromDelimitedFile - with a header" in {
    val delimitedFile = "src/test/resources/mock_data/delimited_file.csv"
    val expectedHeaders = List("id","first_name","last_name","email","gender","ip_address")

    val df = ReaderUtils.readDelimitedFile(fileName = delimitedFile, header = true)

    df.columns.toList shouldEqual expectedHeaders
  }
}
