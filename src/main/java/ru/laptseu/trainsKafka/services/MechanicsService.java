package ru.laptseu.trainsKafka.services;

import lombok.Getter;
import ru.laptseu.trainsKafka.kafka.consumers.StatisticConsumer;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class MechanicsService {
    private static int DURATION_MINUTES_QUERY = 2;
    private int receivedReports = 0;
    List<PercentageMessage> list = new ArrayList<>();

    public void getStatisticAndCalculateState() {
        Thread mechanicsThread = new Thread(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Механики начали работу");
            StatisticConsumer statisticConsumer = new StatisticConsumer();
            List<PercentageMessage> statisticFromKafka = statisticConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY);
            while (!statisticFromKafka.isEmpty()) {
                statisticFromKafka.stream().forEach(percentageMessage -> {
                    list.add(percentageMessage);
                    receivedReports++;
                });
                statisticFromKafka = statisticConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY);
            }
           // statisticConsumer.closeConsumer();
            System.out.println("Механики закончили работу, обработав " + list.size() + " отчетов и определив ресурс парка в " + getState() + " %");
        });
        mechanicsThread.start();
    }

    public int getState() {
        AtomicLong sum = new AtomicLong();
        Thread mechanicsThreadState = new Thread(() -> {
            for (PercentageMessage pm : list) {
                sum.set(+pm.getPercentageOfRecourse());
            }
        });
        mechanicsThreadState.start();
        if (sum.get() != 0 || list.size() != 0) {
            return (int) (sum.get() * 100 / list.size());
        } else {
            return 0;
        }
    }
}
