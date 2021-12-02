package ru.laptseu.trainsKafka.kafka.consumers;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.consumer.*;
import ru.laptseu.trainsKafka.kafka.KafkaPropertiesClass;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;

import java.time.Duration;
import java.util.*;

@Log4j
@NoArgsConstructor
public class OdometerConsumer {
    Properties properties = KafkaPropertiesClass.getPropertiesConsumerOdometer();
    Consumer<String, OdometerInfoFromCarriage> consumer = new KafkaConsumer<String, OdometerInfoFromCarriage>(properties);

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
