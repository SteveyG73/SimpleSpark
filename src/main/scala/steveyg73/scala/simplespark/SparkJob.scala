package com.worldremit.bigdata.scala.simplespark

import org.apache.spark.sql.SparkSession
import scopt.OptionParser


trait SparkJob {

  def main(args: Array[String]): Unit = {

    val parser = makeCommandLineParser()
    parser.parse(args, CommandLineOptions()) match {
      case Some(cmdLineOptions) => run_spark_job(cmdLineOptions)
      case _ =>
    }
  }

  private def run_spark_job(cmdLineOptions: CommandLineOptions): Unit = {
    implicit val spark: SparkSession = SparkSession
      .builder()
      .appName(cmdLineOptions.appName)
      .getOrCreate()

    try {
      spark.sparkContext.setLogLevel("ERROR")
      val accessKeyId = sys.env.getOrElse("AWS_ACCESS_KEY_ID", "unknown")
      val secretAccessKey = sys.env.getOrElse("AWS_SECRET_ACCESS_KEY", "unknown")

      spark.sparkContext.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", accessKeyId)
      spark.sparkContext.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", secretAccessKey)
      execute(cmdLineOptions)
    }
    finally {
      spark.stop()
    }
  }

def execute(cmdLineOpts: CommandLineOptions)(implicit spark: SparkSession): Unit

  def makeCommandLineParser(): OptionParser[CommandLineOptions] = {
    new OptionParser[CommandLineOptions]("Spark Job") {
      head("Spark Job", "0.1")

      opt[String]('m', "master")
        .valueName("<spark master URL>")
        .action((x, c) => c.copy(master = x))

      opt[String]('s', "source")
        .required()
        .valueName("<dir>")
        .action((x, c) => c.copy(source = x))

      opt[Seq[String]](name = "table_dirs")
        .valueName("<dir>,<dir>")
        .action((x, c) => c.copy(tables = x))

      opt[String]('t', "target")
        .required()
        .valueName("<dir>")
        .action((x, c) => c.copy(target = x))

      opt[String]('c', "schema")
        .valueName("<file>")
        .action((x, c) => c.copy(schemaFile = x))

      opt[String]('a', "app")
        .valueName("<name>")
        .action((x, c) => c.copy(appName = x))

      opt[Map[String, String]]("kwargs")
        .valueName("k1=v1,k2=v2...")
        .action((x, c) => c.copy(kwargs = x))
        .text("other arguments")


      help("help").text("General purpose Spark job")
    }
  }


}
