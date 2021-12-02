package ru.laptseu.trainsKafka.kafka.producers;

import lombok.extern.log4j.Log4j;
import ru.laptseu.trainsKafka.kafka.PropertiesClass;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

@Log4j
public class OdometerProducer {

    Properties properties = PropertiesClass.getPropertiesProducerOdometer();
    Producer producer = new KafkaProducer<String, OdometerInfoFromCarriage>(properties);
    Boolean enableLogging = PropertiesClass.isLoggingEnabledInOdometer();

    public OdometerProducer() {
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
