name := "spark-twitter-sentiment"

version := "0.1"

scalaVersion := "2.11.8"

val sparkVersion = "2.2.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-mllib" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming" % sparkVersion
libraryDependencies += "org.apache.bahir" %% "spark-streaming-twitter" % "2.1.0"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.7.0" artifacts (Artifact("stanford-corenlp", "models"), Artifact("stanford-corenlp"))
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3"
