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


### Sending messages to Kafka:
```bash
for (( i=1; i<=10; i++ ))
  do echo "key$$i:value$$i" | ./kafka-console-producer.sh \
  --broker-list localhost:9092 \
  --topic test-pcej \
  --property "parse.key=true" \
  --property "key.separator=:"
done
```