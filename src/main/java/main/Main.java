package main;

public class Main {

    public static void main(String[] args) {
        String MATCHES_DATA_FILEPATH = "src/main/resources/match_data.txt";
        String PLAYERS_DATA_FILEPATH = "src/main/resources/player_data.txt";
        String RESULT_FILEPATH = "src/main/java/main/result.txt";

        BetProcessor betProcessor = new BetProcessor(MATCHES_DATA_FILEPATH, PLAYERS_DATA_FILEPATH, RESULT_FILEPATH);
        betProcessor.run();
    }
}