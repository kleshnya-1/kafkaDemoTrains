package ru.laptseu.trainsKafka.models.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OdometerInfoFromCarriage {
    public OdometerInfoFromCarriage(long carriageId, int odometerKm) {
        this.carriageId = carriageId;
        this.odometerKm = odometerKm;
    }

    @JsonProperty
    private String id = UUID.randomUUID().toString();

    @JsonProperty
    private long carriageId;

    @JsonProperty
    private int odometerKm;


}
