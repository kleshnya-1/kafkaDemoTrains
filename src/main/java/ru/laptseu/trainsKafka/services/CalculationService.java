package ru.laptseu.trainsKafka.services;

import lombok.Getter;
import ru.laptseu.trainsKafka.kafka.consumers.OdometerConsumer;
import ru.laptseu.trainsKafka.kafka.producers.StatisticProducer;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.models.messages.PercentageMessage;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class CalculationService {
    private static int MILLISECOND_TO_SLEEP = 50;
    private static int DURATION_MINUTES_QUERY = 2;
    private static int recourseKm = 1200_000;
    private AtomicInteger calculatingCounter = new AtomicInteger(0);

    private int inQueryCounter = 0;
    private AtomicInteger msPerMessage = new AtomicInteger();

    OdometerConsumer odometerConsumer = new OdometerConsumer();
    StatisticProducer statisticProducer = new StatisticProducer();

    public PercentageMessage calculate(OdometerInfoFromCarriage o) {
        calculatingCounter.incrementAndGet();
        return new PercentageMessage(o.getCarriageId(), 100 - 100 * o.getOdometerKm() / recourseKm);
    }

    public void getCalculateAndSendInfoToKafka() {
        AtomicLong startTime = new AtomicLong(0);
        AtomicLong endTime = new AtomicLong(0);
        Thread calculationCenterThread = new Thread(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<OdometerInfoFromCarriage> odometersFromKafka = odometerConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY);

            while (!odometersFromKafka.isEmpty()) {
                startTime.set(System.currentTimeMillis());

                inQueryCounter = odometersFromKafka.size();
                odometersFromKafka.stream().forEach(odometerInfoFromCarriage -> {
                    try {
                        Thread.sleep(MILLISECOND_TO_SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    statisticProducer.sendCarriageReportToKafka(calculate(odometerInfoFromCarriage));
                });
                endTime.set(System.currentTimeMillis());
                msPerMessage.set((int) ((endTime.get() - startTime.get()) / odometersFromKafka.size()));
                odometersFromKafka = odometerConsumer.getInfoFromKafka(DURATION_MINUTES_QUERY);
                inQueryCounter=0;
            }
            odometerConsumer.closeConsumer();
            System.out.println("Калькулятор закончил работу, обработав " + calculatingCounter + " отчетов");
        });
        calculationCenterThread.start();

    }

    public static int getMillisecondToSleep() {
        return MILLISECOND_TO_SLEEP;
    }
}
