package ru.laptseu.trainsKafka.services;

import ru.laptseu.trainsKafka.kafka.consumers.OdometerConsumer;
import ru.laptseu.trainsKafka.kafka.consumers.StatisticConsumer;
import ru.laptseu.trainsKafka.kafka.producers.StatisticProducer;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.util.ArrayList;
import java.util.List;

public class MechanicsCenter {
    private static int DURATION_MINUTES_QUERY = 10;


    public void getCalculateAndSendInfoToKafka() {
        Thread mechanicsThread = new Thread(() -> {
            List<PercentageMessage> list = new ArrayList<>();
            StatisticConsumer statisticConsumer = new StatisticConsumer();
            while (statisticConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY).isEmpty() == false) {
                List<PercentageMessage> l = statisticConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                l.stream().forEach(percentageMessage -> list.add(percentageMessage));
            }
            statisticConsumer.closeConsumer();
        });
        mechanicsThread.start();
    }

}
