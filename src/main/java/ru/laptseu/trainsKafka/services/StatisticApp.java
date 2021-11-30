package ru.laptseu.trainsKafka.services;

import lombok.RequiredArgsConstructor;
import ru.laptseu.trainsKafka.models.RailwayCarriage;

@RequiredArgsConstructor
public class StatisticApp {

    private  final int NUM_OF_CARRIAGES_FOR_TEST;

public void printInfo(){
    RailwayCarriage railwayCarriage = new RailwayCarriage();


    Thread statisticThread = new Thread(() -> {

        System.out.println("Вагоны: ----------");
        System.out.println("Всего вагонов: "+NUM_OF_CARRIAGES_FOR_TEST);
        System.out.println("Будет отправлено сообщений: "+NUM_OF_CARRIAGES_FOR_TEST*10);
        System.out.println("Интервал сообщений: "+railwayCarriage.getMILLISECOND_TO_SLEEP_MIN()+"-"+railwayCarriage.getMILLISECOND_TO_SLEEP_MAX());

        System.out.println();
        System.out.println("Кафка: ----------");
        System.out.println("Топик пробегов вагонов: ----------");
        System.out.println("Получено: "+);


    });

        statisticThread.start();

    }
}

