package ru.laptseu.trainsKafka.kafka.producers;

import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.producer.*;
import ru.laptseu.trainsKafka.kafka.KafkaPropertiesClass;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.util.Properties;

@Log4j
public class StatisticProducer {

    Properties properties = KafkaPropertiesClass.getPropertiesProducerStatistic();
    Producer producer= new KafkaProducer<String, OdometerInfoFromCarriage>(properties);
    Boolean enableLogging = KafkaPropertiesClass.isLoggingEnabledInStatistic();

    public StatisticProducer() {
    }

    public void sendCarriageReportToKafka(PercentageMessage percentageMessage) {
        ProducerRecord<String, PercentageMessage> record = new ProducerRecord<>("Topic_statistic", "test_key", percentageMessage);

        if (!enableLogging) {
            producer.send(record);
        } else {
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
    }

    public void closeProducer() {
        producer.close();
    }
}
