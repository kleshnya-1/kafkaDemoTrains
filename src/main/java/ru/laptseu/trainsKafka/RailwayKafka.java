package ru.laptseu.trainsKafka;

import lombok.Getter;
import ru.laptseu.trainsKafka.models.RailwayCarriage;
import ru.laptseu.trainsKafka.services.CalculationCenter;
import ru.laptseu.trainsKafka.services.MechanicsCenter;
import ru.laptseu.trainsKafka.services.StatisticService;

@Getter
public class RailwayKafka {
    private static int NUM_OF_CARRIAGES_FOR_TEST=25;

    public static void main(String[] args) {
        System.out.println("starting reporting");
        for (int i = 1; i <= NUM_OF_CARRIAGES_FOR_TEST; i++) {
            RailwayCarriage railwayCarriage = new RailwayCarriage();
            railwayCarriage.setName("name " + i);
            railwayCarriage.setId(i);
            railwayCarriage.setOdometerKm(10_000 + i * 1000);
            railwayCarriage.sendInfoToKafka();
        }

        CalculationCenter calculationCenter = new CalculationCenter();
        calculationCenter.getCalculateAndSendInfoToKafka();

        MechanicsCenter mechanicsCenter = new MechanicsCenter();
        mechanicsCenter.getStatisticAndCalculateState();

        StatisticService statisticService = new StatisticService(1000,NUM_OF_CARRIAGES_FOR_TEST,
                calculationCenter,mechanicsCenter);
        statisticService.printInfo();
    }
}
