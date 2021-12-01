package ru.laptseu.trainsKafka.services;

import lombok.Getter;
import ru.laptseu.trainsKafka.kafka.consumers.StatisticConsumer;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class MechanicsCenter {
    private static int DURATION_MINUTES_QUERY = 10;

    private  int receivedReports =0;

    List<PercentageMessage> list = new ArrayList<>();

    public void getStatisticAndCalculateState() {
        Thread mechanicsThread = new Thread(() -> {

            StatisticConsumer statisticConsumer = new StatisticConsumer();
            while (statisticConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY).isEmpty() == false) {
                List<PercentageMessage> l = statisticConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY);
                l.stream().forEach(percentageMessage -> {
                    list.add(percentageMessage);
                    receivedReports++;
                });
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            statisticConsumer.closeConsumer();
        });
        mechanicsThread.start();
    }

    public int getState() {
        AtomicLong sum= new AtomicLong();
        Thread mechanicsThreadState = new Thread(() -> {
            for (PercentageMessage pm : list){
                sum.set(+pm.getPercentageOfRecourse());
            }
        });
        mechanicsThreadState.start();
        if (sum.get()!=0|| list.size()!=0){
            return (int) (sum.get() /list.size());

        } else {
            return 0;
        }

    }
}
