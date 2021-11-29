package ru.laptseu.trainsKafka.kafka.consumers;

import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.consumer.*;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Log4j
public class StatisticConsumer {

    Properties properties;
    Consumer<String, PercentageMessage> consumer;

    public StatisticConsumer() {
        properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonDeserializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "statistic-consumer-1");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, PercentageMessage.class);
        consumer = new KafkaConsumer<String, PercentageMessage>(properties);
    }

    public List<PercentageMessage> getInfoFromKafka(int num) {
            consumer.subscribe(Collections.singletonList("Topic_statistic"));
            List<PercentageMessage> list = new ArrayList<>();
            ConsumerRecords<String, PercentageMessage> records = consumer.poll(Duration.ofMinutes(num));
            for (ConsumerRecord cr:records){
                list.add((PercentageMessage) cr.value());
            }
            return list;
    }

    public void closeConsumer(){
        consumer.close();
    }
}
