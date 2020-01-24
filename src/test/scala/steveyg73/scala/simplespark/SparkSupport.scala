package steveyg73.scala.simplespark

import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

trait SparkSupport extends FlatSpec with Matchers with BeforeAndAfterAll {

  implicit lazy val spark: SparkSession = {
    SparkSession
      .builder()
      .master("local[2]")
      .config("spark.driver.host", "127.0.0.1")
      .config("spark.driver.bindAddress", "127.0.0.1")
      .appName("spark test example")
      .getOrCreate()
  }

  override def beforeAll(): Unit = {
    val accessKeyId = sys.env.getOrElse("AWS_ACCESS_KEY_ID", "unknown")
    val secretAccessKey = sys.env.getOrElse("AWS_SECRET_ACCESS_KEY", "unknown")

    spark.sparkContext.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", accessKeyId)
    spark.sparkContext.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", secretAccessKey)
  }

  override def afterAll(): Unit = {
    spark.close()
  }
}
