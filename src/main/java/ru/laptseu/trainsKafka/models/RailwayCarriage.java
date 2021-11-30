package ru.laptseu.trainsKafka.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;
import ru.laptseu.trainsKafka.kafka.producers.OdometerProducer;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@NoArgsConstructor
public class RailwayCarriage {
    private int id;
    private int odometerKm;
    private String name;
    private String machinist;

    private final int MILLISECOND_TO_SLEEP_MIN=1000;
    private final int MILLISECOND_TO_SLEEP_MAX=3000;
    private final int NUM_OF_REPORTS_PER_UNIT=10;

    public OdometerInfoFromCarriage getReport() {
        OdometerInfoFromCarriage odometerInfoFromCarriage = new OdometerInfoFromCarriage(id, odometerKm);
        return odometerInfoFromCarriage;
    }

    public void sendInfoToKafka() {
        Thread thread1 = new Thread(() -> {
                OdometerProducer odometerProducer = new OdometerProducer();
                for (int i = 0; i <= NUM_OF_REPORTS_PER_UNIT; i++) {
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt
                                (MILLISECOND_TO_SLEEP_MIN, MILLISECOND_TO_SLEEP_MAX+1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    odometerProducer.sendCarriageReportToKafka(getReport());
                    odometerKm *= 1.15;
                }
                odometerProducer.closeProducer();
        });
        thread1.start();
    }
}
