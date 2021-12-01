package ru.laptseu.trainsKafka.kafka;

import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import lombok.Getter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.util.Properties;

@Getter
public  class PropertiesClass {
    static Properties propertiesProducerOdometer = new Properties();
    static Properties propertiesProducerStatistic = new Properties();

    static Properties propertiesConsumerOdometer = new Properties();
    static Properties propertiesConsumerStatistic = new Properties();

    static {
        propertiesProducerOdometer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        propertiesProducerOdometer.put(ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG, "DEBUG");
        propertiesProducerOdometer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        propertiesProducerOdometer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");

        propertiesProducerStatistic.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        propertiesProducerStatistic.put(ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG, "DEBUG");
        propertiesProducerStatistic.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        propertiesProducerStatistic.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");


        propertiesConsumerOdometer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        propertiesConsumerOdometer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        propertiesConsumerOdometer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonDeserializer");
        propertiesConsumerOdometer.put(ConsumerConfig.GROUP_ID_CONFIG, "odometer-consumer-1");
        propertiesConsumerOdometer.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        propertiesConsumerOdometer.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, OdometerInfoFromCarriage.class);

        propertiesConsumerStatistic.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        propertiesConsumerStatistic.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        propertiesConsumerStatistic.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonDeserializer");
        propertiesConsumerStatistic.put(ConsumerConfig.GROUP_ID_CONFIG, "statistic-consumer-1");
        propertiesConsumerStatistic.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        propertiesConsumerStatistic.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, PercentageMessage.class);

    }

    public static Properties getPropertiesConsumerOdometer() {
        return propertiesConsumerOdometer;
    }
    public static Properties getPropertiesConsumerStatistic() {
        return propertiesConsumerStatistic;
    }
    public static Properties getPropertiesProducerOdometer() {
        return propertiesProducerOdometer;
    }

    public static Properties getPropertiesProducerStatistic() {
        return propertiesProducerStatistic;
    }

}
