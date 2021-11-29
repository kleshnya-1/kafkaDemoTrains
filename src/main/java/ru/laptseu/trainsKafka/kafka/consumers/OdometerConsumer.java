package ru.laptseu.trainsKafka.kafka.consumers;

import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.consumer.*;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;

import java.time.Duration;
import java.util.*;

@Log4j
public class OdometerConsumer {

    Properties properties;
    Consumer<String, OdometerInfoFromCarriage> consumer;

    public OdometerConsumer() {
        properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonDeserializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "odometer-consumer-1");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, OdometerInfoFromCarriage.class);
        consumer = new KafkaConsumer<String, OdometerInfoFromCarriage>(properties);
    }

    public List<OdometerInfoFromCarriage> getInfoFromKafka(int num) {
            consumer.subscribe(Collections.singletonList("Topic_odometer"));
            List<OdometerInfoFromCarriage> list = new ArrayList<>();
            ConsumerRecords<String, OdometerInfoFromCarriage> records = consumer.poll(Duration.ofMinutes(num));
            for (ConsumerRecord cr:records){
                list.add((OdometerInfoFromCarriage) cr.value());
            }
            return list;
    }

    public void closeConsumer(){
        consumer.close();
    }
}
