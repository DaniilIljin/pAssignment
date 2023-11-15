package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
public class Player {
    private UUID Id;
    private long balance = 0;
    private int bets = 0;
    private int wins = 0;
    private final List<Move> moves = new ArrayList<>();

    @Override
    public String toString() {
        return "Player{" +
                "Id=" + Id +
                ", balance=" + balance +
                ", moves=" + moves +
                "}\n";
    }
}
