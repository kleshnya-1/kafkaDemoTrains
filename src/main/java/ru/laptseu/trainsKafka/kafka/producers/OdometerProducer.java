package ru.laptseu.trainsKafka.kafka.producers;

import lombok.extern.log4j.Log4j;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

@Log4j
public class OdometerProducer {

    Properties properties;
    Producer producer;
    Boolean enableLogging = false;

    public OdometerProducer() {
        properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG, "DEBUG");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");
        producer = new KafkaProducer<String, OdometerInfoFromCarriage>(properties);
    }

    public void sendCarriageReportToKafka(OdometerInfoFromCarriage odometerInfoFromCarriage) {

        ProducerRecord<String, OdometerInfoFromCarriage> record = new ProducerRecord<>("Topic_odometer", "test_key", odometerInfoFromCarriage);
        if (!enableLogging) {
            producer.send(record);
        } else {
            /** more info  */
            producer.send(record, (m, e) -> {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    System.out.printf("Produced odometer record to topic %s partition [%d] @ offset %d%n", m.topic(), m.partition(), m.offset());
                }
            });
        }
    }

    public void closeProducer(){
        producer.close();
    }
}
