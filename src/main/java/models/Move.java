package models;

import helpers.MatchResultType;
import helpers.PlayerMoveType;
import lombok.Data;

@Data
public class Move {
    private PlayerMoveType moveType;
    private Match match;
    private Integer amount;
    private MatchResultType matchResult;

    @Override
    public String toString() {
        return "Move{" +
                "moveType=" + moveType +
                ", match=" + match +
                ", amount=" + amount +
                ", matchResult=" + matchResult +
                "}\n";
    }
}
