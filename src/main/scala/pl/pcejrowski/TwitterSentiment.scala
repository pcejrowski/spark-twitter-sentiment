package pl.pcejrowski

import org.apache.spark.ml.linalg.{Vector => MLVector}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object TwitterSentiment {

  def main(args: Array[String]): Unit = {
    val config = new SparkConf()
      .setAppName("twitter-sentiment")
      .setMaster("local[*]")
    val sc = new SparkContext(config)
    sc.setLogLevel("ERROR")

    val ssc = new StreamingContext(config, Seconds(5))
    val stream: DStream[String] = TwitterUtils
      .createStream(ssc, None)
      .window(Seconds(10))
      .map(_.getText)

    emotions(stream)
      .print(10)

    ssc.start()
    ssc.awaitTermination()
  }

  def emotions(stream: DStream[String]): DStream[Int] = {
    stream.map(SentimentAnalyzer.mainSentiment)
  }

}
