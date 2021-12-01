package ru.laptseu.trainsKafka.kafka.consumers;

import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.consumer.*;
import ru.laptseu.trainsKafka.kafka.PropertiesClass;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;

import java.time.Duration;
import java.util.*;

@Log4j
public class OdometerConsumer {

    Properties properties = PropertiesClass.getPropertiesConsumerOdometer();
    Consumer<String, OdometerInfoFromCarriage> consumer = new KafkaConsumer<String, OdometerInfoFromCarriage>(properties);


    public OdometerConsumer() {
        // TODO: 01.12.2021 remove
    }

    public List<OdometerInfoFromCarriage> getInfoFromKafka(int num) {
        consumer.subscribe(Collections.singletonList("Topic_odometer"));
        List<OdometerInfoFromCarriage> list = new ArrayList<>();
        ConsumerRecords<String, OdometerInfoFromCarriage> records = consumer.poll(Duration.ofMinutes(num));
        for (ConsumerRecord cr : records) {
            list.add((OdometerInfoFromCarriage) cr.value());
        }
        return list;
    }

    public void closeConsumer() {
        consumer.close();
    }
}
