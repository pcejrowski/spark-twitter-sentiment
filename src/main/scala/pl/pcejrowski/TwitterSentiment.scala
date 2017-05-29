package pl.pcejrowski

import java.util.Locale

import org.apache.spark.ml.linalg.{Vector => MLVector}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import pl.pcejrowski.Sentiment.Sentiment
import twitter4j.Status

object TwitterSentiment {

  def main(args: Array[String]): Unit = {
    val config = new SparkConf()
      .setAppName("twitter-sentiment").setMaster("local[*]")
    val sc = new SparkContext(config)
    sc.setLogLevel("ERROR")

    val ssc = new StreamingContext(sc, Seconds(5))
    val stream: DStream[Status] = TwitterUtils
      .createStream(ssc, None)
      .window(Minutes(1))

    stream
      .filter { status => status.getLang == "pl" }
      .map { status =>
        val sentiment: Sentiment = SentimentAnalyzer.mainSentiment(status.getText)
        val tags: Array[String] = status
          .getHashtagEntities
          .map(_.getText.toLowerCase(Locale.ENGLISH))

        (status.getText, sentiment.toString, tags)
      }
      .print(100)
    ssc.start()
    ssc.awaitTermination()
  }
}
