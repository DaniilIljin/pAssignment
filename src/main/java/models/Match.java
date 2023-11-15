package models;

import helpers.MatchResultType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@AllArgsConstructor
@Data
public class Match {
    private UUID Id;
    private Double returnRateA;
    private Double returnRateB;
    private MatchResultType matchResult;

    @Override
    public String toString() {
        return "Match{" +
                "Id=" + Id +
                ", returnRateA=" + returnRateA +
                ", returnRateB=" + returnRateB +
                ", matchResult=" + matchResult +
                "}\n";
    }
}
