package ru.laptseu.trainsKafka.kafka.consumers;

import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.consumer.*;
import ru.laptseu.trainsKafka.kafka.PropertiesClass;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Log4j
@NoArgsConstructor
public class StatisticConsumer {
    Properties properties = PropertiesClass.getPropertiesConsumerStatistic();
    Consumer<String, PercentageMessage>  consumer = new KafkaConsumer<String, PercentageMessage>(properties);

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
