package models;

import helpers.MatchResultType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@AllArgsConstructor
@Data
public class Match {
    private UUID Id;
    private float returnRateA;
    private float returnRateB;
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
