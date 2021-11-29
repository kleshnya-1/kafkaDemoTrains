package ru.laptseu.trainsKafka.models.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class PercentageMessage {

    public PercentageMessage(long carriageId, int percentageOfRecourse) {
        this.carriageId = carriageId;
        this.percentageOfRecourse = percentageOfRecourse;
    }

    @JsonProperty
    private String id = UUID.randomUUID().toString();

    @JsonProperty
    private long carriageId;

    @JsonProperty
    private int percentageOfRecourse;


}
