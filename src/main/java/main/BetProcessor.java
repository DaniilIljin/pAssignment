package main;

import helpers.Helper;
import helpers.MatchResultType;
import helpers.PlayerMoveType;
import lombok.Getter;
import models.Match;
import models.Move;
import models.Player;

import java.text.DecimalFormat;
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
        if (legitimateLines.isEmpty()) stringBuilder.append("\n");
        for (String line : legitimateLines) {
            stringBuilder.append(line);
        }
        stringBuilder.append("\n");
        if (illegitimateLines.isEmpty()) stringBuilder.append("\n");
        for (String line : illegitimateLines) {
            stringBuilder.append(line);
        }
        stringBuilder.append("\n").append(casinoBalance);
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
        Double multiplier = move.getMatch().getMatchResult().equals(MatchResultType.A) ?
                move.getMatch().getReturnRateA() : move.getMatch().getReturnRateB();
        return (int) (Math.floor(move.getAmount() * multiplier));
    }
    private boolean isEnoughMoneyOnPlayerBalance(Player player, Move move) {
        return move.getMoveType() != PlayerMoveType.DEPOSIT && ((player.getBalance() - move.getAmount()) < 0);
    }
    private void generateIllegitimatePlayerLine(Player player, Move move) {
        stringBuilder
                .append(player.getId()).append(" ")
                .append(move.getMoveType().getStringValue()).append(" ")
                .append(move.getMatch() == null ? "null" : move.getMatch().getId()).append(" ")
                .append(move.getAmount()).append(" ")
                .append(move.getMatchResult() == null ? "null" : move.getMatchResult().getStringValue()).append("\n");
        illegitimateLines.add(stringBuilder.toString());
        stringBuilder.setLength(0);
    }
    private void generateLegitimatePlayerLine(Player player) {
        double winR = ((double) player.getWins()) / player.getBets();
        String winRate = new DecimalFormat("#.##")
                .format(winR).replace(".",",");
        stringBuilder
                .append(player.getId()).append(" ")
                .append(player.getBalance()).append(" ")
                .append(winRate).append("\n");
        legitimateLines.add(stringBuilder.toString());
        stringBuilder.setLength(0);
    }
    public List<Player> createPlayersList(Map<String, Match> foundMatches) {
        List<String> lines = Helper.readFromFile(PLAYERS_DATA_FILEPATH);
        Map<String, Player> players = new HashMap<>();
        for (String line : lines) {
            String[] data = line.split(",");
            if (validatePlayerData(data)) {
                Move move = new Move();
                move.setMoveType(Helper.findPlayerMoveType(data[1]));
                move.setAmount(Integer.valueOf(data[3]));
                addMoveToPlayer(data[0], players, move);
                addMatchIfMoveTypeIsBet(foundMatches, move, data);
            }
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
            throw new RuntimeException("Match with this Id does not exist: " + Id);
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
            if (validateMatchData(data)) {
                Match match = new Match(
                        UUID.fromString(data[0]),
                        Double.valueOf(data[1]),
                        Double.valueOf(data[2]),
                        Helper.findMatchResultType(data[3])

                );
                matches.put(data[0], match);
            }
        }
        return matches;
    }
    private boolean validateMatchData(String[] data) {
        return data.length == 4;
    }
    private boolean validatePlayerData(String[] data) {
        return true;
    }
}