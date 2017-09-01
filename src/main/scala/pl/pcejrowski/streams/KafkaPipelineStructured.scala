package pl.pcejrowski.streams

import org.apache.spark.SparkConf
import org.apache.spark.ml.linalg.{Vector => MLVector}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Dataset, Row, SparkSession}


object KafkaPipelineStructured {

  val bootstrapServers = ""

  def main(args: Array[String]): Unit = {
    val config = new SparkConf()
      .setAppName("kafka-pipeline-structured")
      .setMaster("local[*]")
      .set("spark.sql.streaming.checkpointLocation", ".checkpoints")

    val spark = SparkSession.builder().config(config).getOrCreate()
    import spark.implicits._

    val userSchema = new StructType().add("key", "string").add("value", "string")
    val static: Dataset[Row] = spark.readStream
      .schema(userSchema)
      .csv("./src/main/resources/csvs")
    static.printSchema()

    val dynamic: Dataset[Row] = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option("subscribe", "test")
      .load()
      .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
    dynamic.printSchema()

    static.union(dynamic)
      .as[(String, String)]
      .writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option("topic", "test")
      .start()
      .awaitTermination()
  }
}
