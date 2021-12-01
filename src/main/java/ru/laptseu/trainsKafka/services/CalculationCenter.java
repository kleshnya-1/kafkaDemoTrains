package ru.laptseu.trainsKafka.services;

import lombok.Getter;
import ru.laptseu.trainsKafka.kafka.consumers.OdometerConsumer;
import ru.laptseu.trainsKafka.kafka.producers.StatisticProducer;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class CalculationCenter {
    private static int MILLISECOND_TO_SLEEP = 1000;
    private static int DURATION_MINUTES_QUERY = 10;
    private static int recourseKm = 1200_000;
    private  int calculatingCounter = 0;
    private  int inQueryCounter = 0;
    private  AtomicInteger msPerMessage = new AtomicInteger();


    OdometerConsumer odometerConsumer = new OdometerConsumer();
    StatisticProducer statisticProducer = new StatisticProducer();

    public PercentageMessage calculate(OdometerInfoFromCarriage o) {
        calculatingCounter++;
        return new PercentageMessage(o.getCarriageId(), 100 - 100 * o.getOdometerKm() / recourseKm);
    }

    public void getCalculateAndSendInfoToKafka() {
        AtomicLong startTime = new AtomicLong(0);
        AtomicLong endTime = new AtomicLong(0);

        Thread calculationCenterThread = new Thread(() -> {

            while (odometerConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY).isEmpty() == false) {
                       startTime.set(System.currentTimeMillis());

                List<OdometerInfoFromCarriage> l = odometerConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY);
                inQueryCounter = l.size();
                l.stream().forEach(odometerInfoFromCarriage -> {
                    try {
                        Thread.sleep(MILLISECOND_TO_SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    statisticProducer.sendCarriageReportToKafka(calculate(odometerInfoFromCarriage));
                    endTime.set(System.currentTimeMillis());
                    msPerMessage.set((int) ((endTime.get() - startTime.get()) / l.size()));
                });
            }
            odometerConsumer.closeConsumer();
        });
        calculationCenterThread.start();
    }
}
