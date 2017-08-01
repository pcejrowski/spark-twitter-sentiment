package pl.pcejrowski

import org.apache.spark.ml.linalg.{Vector => MLVector}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import twitter4j.Status

object TwitterSentiment {

  def main(args: Array[String]): Unit = {
    val config = new SparkConf()
      .setAppName("twitter-sentiment")
      .setMaster("local[*]")
    val sc = new SparkContext(config)
    sc.setLogLevel("ERROR")

    val ssc = new StreamingContext(config, Seconds(5))
    val stream: DStream[Status] = TwitterUtils
      .createStream(ssc, None)
      .window(Minutes(1))

    stream
      .map(_.getText)
      .print(10)

    ssc.start()
    ssc.awaitTermination()
  }
}
