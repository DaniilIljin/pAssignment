package mainFlow;

import helpers.MatchResultType;
import main.BetProcessor;
import models.Match;
import models.Move;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BetProcessorTests {
    private final String MATCHES_DATA_FILEPATH = "src/test/resources/match_data.txt";
    private final String PLAYERS_DATA_FILEPATH = "src/test/resources/player_data.txt";
    private final String RESULT_FILEPATH = "src/test/java/mainFlow/result.txt";


    @Test
    public void givenInputFiles_whenCreateMatchesMethodIsCalled_thenReturnExpectedMatches() {
        // Arrange
        BetProcessor betProcessor = new BetProcessor(MATCHES_DATA_FILEPATH, PLAYERS_DATA_FILEPATH, RESULT_FILEPATH);
        Map<String, Match> expectedMatches = new HashMap<>(){{
            put("abae2255-4255-4304-8589-737cdff61640",
                    new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61640"),
                            1.45, 0.75, MatchResultType.A));
            put("a3815c17-9def-4034-a21f-65369f6d4a56",
                    new Match(UUID.fromString("a3815c17-9def-4034-a21f-65369f6d4a56"),
                            4.34, 0.23, MatchResultType.B));
            put("2b20e5bb-9a32-4d33-b304-a9c7000e6de9",
                    new Match(UUID.fromString("2b20e5bb-9a32-4d33-b304-a9c7000e6de9"),
                            0.54, 1.85, MatchResultType.DRAW));
            put("d6c8b5a4-31ce-4bf8-8511-206cfd693440",
                    new Match(UUID.fromString("d6c8b5a4-31ce-4bf8-8511-206cfd693440"),
                            0.88, 1.17, MatchResultType.B));
            put("0037ae59-61ea-46c3-88a8-8ca705acde10",
                    new Match(UUID.fromString("0037ae59-61ea-46c3-88a8-8ca705acde10"),
                            2.43, 0.41, MatchResultType.B));
        }};

        // Act
        Map<String, Match> foundMatches = betProcessor.createMatchesMap();


        // Assert
        assertEquals(foundMatches, expectedMatches);
    }

    @Test
    public void givenInputFiles_whenCreatePlayersListMethodCalled_thenReturnExpectedPlayers() {
        // Arrange
        BetProcessor betProcessor = new BetProcessor(MATCHES_DATA_FILEPATH, PLAYERS_DATA_FILEPATH, RESULT_FILEPATH);
        List<Player> expectedPlayers = new ArrayList<>(){};
        Player player1 = new Player();
        player1.setId(UUID.fromString("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4"));
        player1.getMoves().addAll(List.of(new Move(), new Move(), new Move(), new Move(), new Move()));
        Player player2 = new Player();
        player2.setId(UUID.fromString("4925ac98-833b-454b-9342-13ed3dfd3ccf"));
        player2.getMoves().addAll(List.of(new Move(), new Move()));
        expectedPlayers.addAll(List.of(player1, player2));

        // Act
        Map<String, Match> foundMatches = betProcessor.createMatchesMap();
        List<Player> actualPlayers = betProcessor.createPlayersList(foundMatches);


        // Assert
        for (Player actualPlayer : actualPlayers) {
            System.out.println(actualPlayer.getId());
        }
        assertEquals(actualPlayers.size(), expectedPlayers.size());
        for (Player expectedPlayer : expectedPlayers) {
            System.out.println(actualPlayers);
            Optional<Player> actualPlayer = actualPlayers.stream()
                    .filter(p -> p.getId().equals(expectedPlayer.getId())).findFirst();
            assertTrue(actualPlayer.isPresent());
            assertEquals(actualPlayer.get().getId(), expectedPlayer.getId());
            assertEquals(actualPlayer.get().getMoves().size(), expectedPlayer.getMoves().size());
        }
    }

    @Test
    public void givenInputFiles_whenAnalyseDataMethodCalled_thenBetProcessorHasCorrectData() {
        // Arrange
        BetProcessor betProcessor = new BetProcessor(MATCHES_DATA_FILEPATH, PLAYERS_DATA_FILEPATH, RESULT_FILEPATH);
        List<String> expectedLegitimateLines = new ArrayList<>(List.of(
                "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 4475 0,25\n",
                "4925ac98-833b-454b-9342-13ed3dfd3ccf 408 1\n"
        )){};
        List<String> expectedIllegitimateLines = new ArrayList<>(){};

        // Act
        List<Player> players = betProcessor.collectData();
        betProcessor.analyseData(players);

        // Assert
        assertEquals(betProcessor.getLegitimateLines(), expectedLegitimateLines);
        assertEquals(betProcessor.getIllegitimateLines(), expectedIllegitimateLines);
    }
}
