package ru.laptseu.trainsKafka.services;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.laptseu.trainsKafka.kafka.KafkaPropertiesClass;
import ru.laptseu.trainsKafka.models.RailwayCarriage;
import ru.laptseu.trainsKafka.models.messages.OdometerInfoFromCarriage;

import java.util.Collections;
import java.util.Properties;

@RequiredArgsConstructor
public class StatisticService {

    private final int REPORTING_INTERVAL_MS;
    private final int NUM_OF_CARRIAGES_FOR_TEST;
    private int MESSAGES_PER_CARRIAGE = 10;
    private final CalculationService calculationService;
    private final MechanicsService mechanicsService;


    Properties propertiesConsumerOdometer = KafkaPropertiesClass.getPropertiesConsumerOdometer();
    Consumer<String, OdometerInfoFromCarriage> consumerOdometer = new KafkaConsumer<String, OdometerInfoFromCarriage>(propertiesConsumerOdometer);

    Properties propertiesConsumerStatistic = KafkaPropertiesClass.getPropertiesConsumerStatistic();
    Consumer<String, OdometerInfoFromCarriage> consumerStatistic = new KafkaConsumer<String, OdometerInfoFromCarriage>(propertiesConsumerOdometer);

    public void printInfo() {
        RailwayCarriage railwayCarriage = new RailwayCarriage();
        consumerOdometer.subscribe(Collections.singletonList("Topic_odometer"));
        consumerStatistic.subscribe(Collections.singletonList("Topic_statistic"));


        Thread statisticThread = new Thread(() -> {
            for (int i = 0; i <= (2 + railwayCarriage.getMILLISECOND_TO_SLEEP_MAX() * MESSAGES_PER_CARRIAGE / REPORTING_INTERVAL_MS); i++) {
                System.out.println("------------------------------------");
                System.out.println("Парк вагонов:");
                System.out.println("Всего вагонов: " + NUM_OF_CARRIAGES_FOR_TEST);
                System.out.println("Будет отправлено сообщений: " + NUM_OF_CARRIAGES_FOR_TEST * MESSAGES_PER_CARRIAGE);
                System.out.println("Интервал сообщений: " + railwayCarriage.getMILLISECOND_TO_SLEEP_MIN() + "-" + railwayCarriage.getMILLISECOND_TO_SLEEP_MAX() + " ms");
//                System.out.println();
//                System.out.println("Кафка: ----------");
//                System.out.println("Сообщений в топике пробегов: "+);
//                System.out.println("Сообщений в топике ресурса: "+((List)consumerStatistic.poll(Duration.ofMinutes(2))).size());

                System.out.println("Центр статистики: ----------");
                System.out.println("Подсчитано отчетов: " + calculationService.getCalculatingCounter());
                System.out.println("Сообщений в очереди: " + calculationService.getInQueryCounter()+ " (время паузы "+ calculationService.getMillisecondToSleep()+"ms)") ;
                System.out.println("Скорость обработки: " + calculationService.getMsPerMessage() + " мс/сообщение");
                System.out.println("Центр механики: ----------");
                System.out.println("Получено отчетов: " + mechanicsService.getReceivedReports());
                System.out.println("Ресурс парка: " + mechanicsService.getState());
                System.out.println("____________________________________");
                try {
                    Thread.sleep(REPORTING_INTERVAL_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        statisticThread.start();

    }
}

