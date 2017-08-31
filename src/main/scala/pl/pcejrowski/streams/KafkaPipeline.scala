package pl.pcejrowski.streams

import com.github.benfradet.spark.kafka.writer._
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.spark.ml.linalg.{Vector => MLVector}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


object KafkaPipeline {

  def main(args: Array[String]): Unit = {
    val config = new SparkConf()
      .setAppName("twitter-sentiment")
      .setMaster("local[*]")
    val sc = new SparkContext(config)
    sc.setLogLevel("ERROR")
    sc.setCheckpointDir("./.checkpoints")

    val ssc = new StreamingContext(sc, Seconds(5))

    val kafkaParams = Map("bootstrap.servers" -> "",
      "key.deserializer" -> classOf[StringDeserializer].getName,
      "value.deserializer" -> classOf[StringDeserializer].getName,
      "key.serializer" -> classOf[StringSerializer].getName,
      "value.serializer" -> classOf[StringSerializer].getName,
      "group.id" -> "pawel-test")

    KafkaUtils
      .createDirectStream(ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](Seq("test-pcej"), kafkaParams))
      .flatMap(msg => toTokens(msg))
      .mapWithState(stateSpec)
      .filter(_.isDefined)
      .map(_.get)
      .writeToKafka(kafkaParams, record => new ProducerRecord[String, String]("oslo-workshop", record._1, record._2))

    ssc.start()
    ssc.awaitTermination()
  }

  private def toTokens(msg: ConsumerRecord[String, String]): Map[String, String] = {
    msg
      .value()
      .split(" ")
      .map(msg.key() -> _)
      .toMap
  }

  def stateSpec: StateSpec[String, String, Map[String, Int], Option[(String, String)]] = {
    StateSpec
      .function(stateTransformer _)
      .timeout(Seconds(30))
  }

  def stateTransformer(key: String, valueOpt: Option[String], state: State[Map[String, Int]]): Option[(String, String)] = {
    valueOpt
      .foreach { value =>
        val newState: Map[String, Int] = state.getOption() match {
          case Some(st) =>
            st + (value -> (st.getOrElse(value, 0) + 1))
          case None =>
            Map(value -> 1)
        }
        state.update(newState)
      }
    state
      .getOption()
      .map(phrases => key -> phrases.toList.sortBy(-_._2).map(_._1).head)
  }

}
