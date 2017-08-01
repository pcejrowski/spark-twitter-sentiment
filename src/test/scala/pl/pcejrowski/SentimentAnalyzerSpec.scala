package pl.pcejrowski

import org.scalatest.{FunSpec, Matchers}

class SentimentAnalyzerSpec extends FunSpec with Matchers {

  describe("sentiment analyzer") {

    it("should return POSITIVE when input has positive emotion") {
      val input = "Scala is a great general purpose language."
      val sentiment = SentimentAnalyzer.mainSentiment(input)
      sentiment should be(3)
    }

    it("should return NEGATIVE when input has negative emotion") {
      val input = "The weather in Oslo is not great"
      val sentiment = SentimentAnalyzer.mainSentiment(input)
      sentiment should be(1)
    }

    it("should return NEUTRAL when input has no emotion") {
      val input = "I am attending a workshop"
      val sentiment = SentimentAnalyzer.mainSentiment(input)
      sentiment should be(2)
    }
  }
}