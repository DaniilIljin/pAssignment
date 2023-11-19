package main;

import exceptions.NotAbleToParseDataException;
import exceptions.NotEnoughDataException;
import exceptions.NotExistingObjectException;
import helpers.Helper;
import helpers.MatchResultType;
import helpers.PlayerMoveType;
import lombok.Getter;
import models.Match;
import models.Move;
import models.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BetProcessor {
    private final String MATCHES_DATA_FILEPATH;
    private final String PLAYERS_DATA_FILEPATH;
    private final String RESULT_FILEPATH;
    private long casinoBalance = 0;
    private long currentPlayerCasinoBalance = 0;
    @Getter
    private final List<String> legitimateLines = new ArrayList<>();
    @Getter
    private final List<String> illegitimateLines = new ArrayList<>();
    private final StringBuilder stringBuilder = new StringBuilder();

    public BetProcessor(String matcheFilePath, String playersFilePath, String resultFilePath) {
        this.MATCHES_DATA_FILEPATH = matcheFilePath;
        this.PLAYERS_DATA_FILEPATH = playersFilePath;
        this.RESULT_FILEPATH = resultFilePath;
    }

    public void run(){
        List<Player> players = collectData();
        analyseData(players);
        generateResult();
    }
    public List<Player> collectData() {
        Map<String, Match> foundMatches = createMatchesMap();
        return createPlayersList(foundMatches);
    }
    public void analyseData(List<Player> players) {
        for (Player player : players) {
            generatePlayerLine(player);
        }
    }
    private void generatePlayerLine(Player player) {
        for (Move move : player.getMoves()) {
            if (isEnoughMoneyOnPlayerBalance(player, move)) {
                //if player has not enough money to WITHDRAW or BET,
                //then player's move will be considered illegal
                generateIllegitimatePlayerLine(player, move);
                currentPlayerCasinoBalance = 0;
                return;
            }
            executePlayerMove(player, move);
        }
        casinoBalance += currentPlayerCasinoBalance;
        currentPlayerCasinoBalance = 0;
        generateLegitimatePlayerLine(player);
    }
    private void generateResult() {
        Helper.addLinesToResultString(stringBuilder, legitimateLines);
        Helper.addLinesToResultString(stringBuilder, illegitimateLines);
        stringBuilder.append(casinoBalance);
        Helper.createAndWriteToFile(RESULT_FILEPATH, stringBuilder.toString());
        stringBuilder.setLength(0);
    }
    private void executePlayerMove(Player player, Move move) {
        if(move.getMoveType().equals(PlayerMoveType.BET)){
            executePlayerBet(player, move);
        } else if (move.getMoveType().equals(PlayerMoveType.WITHDRAW)){
            executePlayerWithdraw(player, move);
        } else {
            executePlayerDeposit(player, move);
        }
    }
    private void executePlayerBet(Player player, Move move) {
        player.setBets(player.getBets() + 1);
        if (move.getMatch().getMatchResult().equals(MatchResultType.DRAW)){
            // because of the draw player balance does not change
            return;
        } else if (move.getMatchResult().equals(move.getMatch().getMatchResult())) {
            player.setWins(player.getWins() + 1);
            Integer wonMoney = calculateWin(move);
            player.setBalance(player.getBalance() + wonMoney);
            currentPlayerCasinoBalance -= wonMoney;
        } else {
            player.setBalance(player.getBalance() - move.getAmount());
            currentPlayerCasinoBalance += move.getAmount();
        }
    }
    private void executePlayerWithdraw(Player player, Move move) {
        player.setBalance(player.getBalance() - move.getAmount());
    }
    private void executePlayerDeposit(Player player, Move move) {
        player.setBalance(player.getBalance() + move.getAmount());
    }
    private Integer calculateWin(Move move) {
        float multiplier = move.getMatch().getMatchResult().equals(MatchResultType.A) ?
                move.getMatch().getReturnRateA() : move.getMatch().getReturnRateB();
        return (int) (Math.floor(move.getAmount() * multiplier));
    }
    private boolean isEnoughMoneyOnPlayerBalance(Player player, Move move) {
        return move.getMoveType() != PlayerMoveType.DEPOSIT && ((player.getBalance() - move.getAmount()) < 0);
    }
    private void generateIllegitimatePlayerLine(Player player, Move move) {
        stringBuilder
                .append(player.getId()).append(" ")
                .append(move.getMoveType().name()).append(" ")
                .append(move.getMatch() == null ? "null" : move.getMatch().getId()).append(" ")
                .append(move.getAmount()).append(" ")
                .append(move.getMatchResult() == null ? "null" : move.getMatchResult().name()).append("\n");
        illegitimateLines.add(stringBuilder.toString());
        stringBuilder.setLength(0);
    }
    private void generateLegitimatePlayerLine(Player player) {
        BigDecimal winRate = new BigDecimal(player.getWins())
                .divide(new BigDecimal(player.getBets()), 2, RoundingMode.HALF_UP);
        stringBuilder
                .append(player.getId()).append(" ")
                .append(player.getBalance()).append(" ")
                .append(winRate.toString().replace(".",",")).append("\n");
        legitimateLines.add(stringBuilder.toString());
        stringBuilder.setLength(0);
    }
    public List<Player> createPlayersList(Map<String, Match> foundMatches) {
        List<String> lines = Helper.readFromFile(PLAYERS_DATA_FILEPATH);
        Map<String, Player> players = new HashMap<>();
        for (String line : lines) {
            String[] data = line.split(",");
            validatePlayerData(data);
            Move move = new Move();
            move.setMoveType(Helper.findPlayerMoveType(data[1]));
            move.setAmount(Integer.valueOf(data[3]));
            addMoveToPlayer(data[0], players, move);
            addMatchIfMoveTypeIsBet(foundMatches, move, data);
        }
        return players.values().stream().toList();
    }
    private void addMatchIfMoveTypeIsBet(Map<String, Match> foundMatches, Move move, String[] data) {
        if (move.getMoveType() == PlayerMoveType.BET) {
            move.setMatch(getMatchById(data[2], foundMatches));
            move.setMatchResult(Helper.findMatchResultType(data[4]));
        }
    }
    private void addMoveToPlayer(String playerId, Map<String, Player> players, Move move) {
        getPlayerById(playerId, players).getMoves().add(move);
    }
    private Match getMatchById(String Id, Map<String, Match> matches) {
        Match res = matches.get(Id);
        if (res == null) {
            throw new NotExistingObjectException("Match with this Id does not exist: " + Id);
        }
        return res;
    }
    private Player getPlayerById(String id, Map<String, Player> players) {
        Player res;
        Player existedPlayer = players.get(id);
        if (existedPlayer != null) {
            res = existedPlayer;
        } else {
            res = new Player();
            res.setId(UUID.fromString(id));
            players.put(id, res);
        }
        return res;
    }
    public Map<String, Match> createMatchesMap() {
        List<String> lines = Helper.readFromFile(MATCHES_DATA_FILEPATH);
        HashMap<String, Match> matches = new HashMap<>();
        for (String line : lines) {
            String[] data = line.split(",");
            validateMatchData(data);
            Match match = new Match(
                    UUID.fromString(data[0]),
                    Float.parseFloat(data[1]),
                    Float.parseFloat(data[2]),
                    Helper.findMatchResultType(data[3])

            );
            matches.put(data[0], match);
        }
        return matches;
    }
    private void validateMatchData(String[] data) {
        if (data.length != 4) throw new NotEnoughDataException("Match line of data must contain 4 elements: " + data);
        try {
            Float.parseFloat(data[1]);
            Float.parseFloat(data[2]);
        } catch (NumberFormatException e) {
            throw new NotAbleToParseDataException("Can not parse rate in given data: " + data[1] + " or " + data[2]);
        }
    }
    private void validatePlayerData(String[] data) {
        try {
            Integer.valueOf(data[3]);
        } catch (NumberFormatException e){
            throw new NotAbleToParseDataException("Can not parse amount in given data: " + data[3]);
        }
    }
}
