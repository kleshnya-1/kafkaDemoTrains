package ru.laptseu.trainsKafka.kafka.producers;

import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.producer.*;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.util.Properties;

@Log4j
public class StatisticProducer {

    Properties properties;
    Producer producer;

    public StatisticProducer() {
        properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG, "DEBUG");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");
        producer = new KafkaProducer<String, OdometerInfoFromCarriage>(properties);
    }

    public void sendCarriageReportToKafka(PercentageMessage percentageMessage) {
               ProducerRecord<String, PercentageMessage> record = new ProducerRecord<>("Topic_statistic", "test_key", percentageMessage);
//        producer.send(record);
        /** more info  */
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata m, Exception e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    System.out.printf("Produced statistic record to topic %s partition [%d] @ offset %d%n", m.topic(), m.partition(), m.offset());
                }
            }
        });
    }


     public void closeProducer(){
    producer.close();
     }
}
