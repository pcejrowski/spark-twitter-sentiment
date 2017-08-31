package pl.pcejrowski

import org.apache.spark.ml.linalg.{Vector => MLVector}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object KafkaUsage {

  def main(args: Array[String]): Unit = {
    val config = new SparkConf()
      .setAppName("twitter-sentiment")
      .setMaster("local[*]")
    val sc = new SparkContext(config)
    sc.setLogLevel("ERROR")

    val ssc = new StreamingContext(sc, Seconds(5))

    val kafkaParams = Map("bootstrap.servers" -> "XXX",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "group.id" -> "pawel-test")
    KafkaUtils.createDirectStream(ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Seq("test-pcej"), kafkaParams))
      .map(_.value())
      .print(10)

    ssc.start()
    ssc.awaitTermination()
  }

}
