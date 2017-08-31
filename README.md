# spark-twitter-sentiment
Sentiment anlysis of Twitter data using Apache Spark framework


To run this  app Twitter authentication has to be established here: https://apps.twitter.com.
Then, you can add the following vm-options:
```bash
-Dtwitter4j.oauth.consumerKey=XXX \
-Dtwitter4j.oauth.consumerSecret=XXX \
-Dtwitter4j.oauth.accessToken=XXX \
-Dtwitter4j.oauth.accessTokenSecret=XXX
```

## Kafka fundamentals
`cd $KAFKA10_HOME/bin`
### List topics
```bash
./kafka-topics.sh --list \
  --zookeeper localhost:2181/kafka
```

### Creating topic
```bash
./kafka-topics.sh --create \
  --topic topic-name \
  --replication-factor 1 \
  --partitions 1 \
  --zookeeper localhost:2181/kafka
```

### Listining on topic
```bash
./kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic topic-name \
  --property print.key=true \
  --property key.separator=":"
```

### Sending keyed messages to Kafka:
```bash
./kafka-console-producer.sh \
  --broker-list localhost:9092 \
  --topic topic-name \
  --property "parse.key=true" \
  --property "key.separator=:"
```

TODO:
* create workshop template with .g8 replacing kafka address and twitter creadentials 
* include git history into g8, so thet groll can be used
* create good slides
* create docker compose to have private kafka and zookeeper
* replace sentiment analyzer with generate MLlib model