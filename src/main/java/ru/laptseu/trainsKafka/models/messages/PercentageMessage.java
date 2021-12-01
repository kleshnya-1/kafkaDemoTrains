package ru.laptseu.trainsKafka.models.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
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
