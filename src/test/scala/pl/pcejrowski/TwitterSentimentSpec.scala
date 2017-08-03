package pl.pcejrowski

import com.holdenkarau.spark.testing.StreamingSuiteBase
import org.scalatest.FunSuite

class TwitterSentimentSpec extends FunSuite with StreamingSuiteBase {

  test("TwitterSentiment should check emotions") {
    val batch1 = List[String]("Scala is a great general purpose language.", "Scala is a great general purpose language.")
    val batch2 = List("Scala is a great general purpose language.")
    val input: List[List[String]] = List(batch1, batch2)
    val expected = List(List(3, 3), List(3))
    testOperation[String, Int](input, TwitterSentiment.emotions(_), expected)
  }
}
