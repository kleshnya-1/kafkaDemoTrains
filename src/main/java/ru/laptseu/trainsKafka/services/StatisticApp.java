package ru.laptseu.trainsKafka.services;

import lombok.RequiredArgsConstructor;
import ru.laptseu.trainsKafka.models.RailwayCarriage;

@RequiredArgsConstructor
public class StatisticApp {

    private final int REPORTING_INTERVAL_MS;
    private  final int NUM_OF_CARRIAGES_FOR_TEST;
    private   int MESSAGES_PER_CARRIAGE=10;
    private  final CalculationCenter calculationCenter;
    private  final MechanicsCenter mechanicsCenter;

public void printInfo(){
    RailwayCarriage railwayCarriage = new RailwayCarriage();


    Thread statisticThread = new Thread(() -> {
        for (int i=0; i<= (2+railwayCarriage.getMILLISECOND_TO_SLEEP_MAX()*MESSAGES_PER_CARRIAGE/REPORTING_INTERVAL_MS);i++){
            System.out.println();
            System.out.println("Вагоны: ----------");
            System.out.println("Всего вагонов: "+NUM_OF_CARRIAGES_FOR_TEST);
            System.out.println("Будет отправлено сообщений: "+NUM_OF_CARRIAGES_FOR_TEST*MESSAGES_PER_CARRIAGE);
            System.out.println("Интервал сообщений: "+railwayCarriage.getMILLISECOND_TO_SLEEP_MIN()+"-"+railwayCarriage.getMILLISECOND_TO_SLEEP_MAX()+" ms");

            System.out.println();
            System.out.println("Кафка: ----------");
            System.out.println("Топик пробегов вагонов: ----------");

//        System.out.println("Получено: "+calculationCenter.);
            System.out.println();
            System.out.println("Центр статистики: ----------");
            System.out.println("Подсчитано вагонов: "+calculationCenter.getCalculatingCounter());
            System.out.println("Сообщений в очереди: "+calculationCenter.getInQueryCounter());
            System.out.println("Скорость обработки: "+calculationCenter.getMsPerMessage()+" мс/сообщение");
            System.out.println();
            System.out.println("Центр механики: ----------");
            System.out.println("Получено отчетов: "+mechanicsCenter.getReceivedReports());
            System.out.println("Ресурс парка: "+mechanicsCenter.getState());

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

