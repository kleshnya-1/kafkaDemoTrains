package ru.laptseu.trainsKafka.services;

import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;
import ru.laptseu.trainsKafka.kafka.consumers.OdometerConsumer;
import ru.laptseu.trainsKafka.kafka.producers.StatisticProducer;

import java.util.List;

public class CalculationCenter {
    private static int MILLISECOND_TO_SLEEP = 1000;
    private static int DURATION_MINUTES_QUERY = 10;
    private static int recourseKm = 1200_000;

    public PercentageMessage calculate(OdometerInfoFromCarriage o) {
        return new PercentageMessage(o.getCarriageId(), 100 - 100 * o.getOdometerKm() / recourseKm);
    }

    public void getCalculateAndSendInfoToKafka() {
        Thread calculationCenterThread = new Thread(() -> {
            OdometerConsumer odometerConsumer = new OdometerConsumer();
            StatisticProducer statisticProducer = new StatisticProducer();
            while (odometerConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY).isEmpty()==false){
                List<OdometerInfoFromCarriage> l = odometerConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY);
                l.stream().forEach(odometerInfoFromCarriage -> {
                    try {
                        Thread.sleep(MILLISECOND_TO_SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    statisticProducer.sendCarriageReportToKafka(calculate(odometerInfoFromCarriage));
                });
            }
            odometerConsumer.closeConsumer();
        });
        calculationCenterThread.start();
    }
}
