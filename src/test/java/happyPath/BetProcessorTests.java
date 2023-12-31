package happyPath;

import helpers.MatchResultType;
import main.BetProcessor;
import models.Match;
import models.Move;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BetProcessorTests {
    private BetProcessor getNewBetProcessor(){
        String MATCHES_DATA_FILEPATH = "src/test/resources/match_data.txt";
        String PLAYERS_DATA_FILEPATH = "src/test/resources/player_data.txt";
        String RESULT_FILEPATH = "src/test/java/mainFlow/result.txt";

        return new BetProcessor(MATCHES_DATA_FILEPATH, PLAYERS_DATA_FILEPATH, RESULT_FILEPATH);
    }

    @Test
    public void givenInputFiles_whenCreateMatchesMethodIsCalled_thenReturnExpectedMatches() {
        // Arrange
        BetProcessor betProcessor = getNewBetProcessor();
        Map<String, Match> expectedMatches = new HashMap<>(){{
            put("abae2255-4255-4304-8589-737cdff61640",
                    new Match(UUID.fromString("abae2255-4255-4304-8589-737cdff61640"),
                            1.45f, 0.75f, MatchResultType.A));
            put("a3815c17-9def-4034-a21f-65369f6d4a56",
                    new Match(UUID.fromString("a3815c17-9def-4034-a21f-65369f6d4a56"),
                            4.34f, 0.23f, MatchResultType.B));
            put("2b20e5bb-9a32-4d33-b304-a9c7000e6de9",
                    new Match(UUID.fromString("2b20e5bb-9a32-4d33-b304-a9c7000e6de9"),
                            0.54f, 1.85f, MatchResultType.DRAW));
            put("d6c8b5a4-31ce-4bf8-8511-206cfd693440",
                    new Match(UUID.fromString("d6c8b5a4-31ce-4bf8-8511-206cfd693440"),
                            0.88f, 1.17f, MatchResultType.B));
            put("0037ae59-61ea-46c3-88a8-8ca705acde10",
                    new Match(UUID.fromString("0037ae59-61ea-46c3-88a8-8ca705acde10"),
                            2.43f, 0.41f, MatchResultType.B));
        }};

        // Act
        Map<String, Match> foundMatches = betProcessor.createMatchesMap();


        // Assert
        assertEquals(expectedMatches, foundMatches);
    }

    @Test
    public void givenInputFiles_whenCreatePlayersListMethodCalled_thenReturnExpectedPlayers() {
        // Arrange
        BetProcessor betProcessor = getNewBetProcessor();
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
            assertEquals(expectedPlayer.getId(), actualPlayer.get().getId());
            assertEquals(expectedPlayer.getMoves().size(), actualPlayer.get().getMoves().size());
        }
    }

    @Test
    public void givenInputFiles_whenAnalyseDataMethodCalled_thenBetProcessorHasCorrectData() {
        // Arrange
        BetProcessor betProcessor = getNewBetProcessor();
        List<String> expectedLegitimateLines = new ArrayList<>(List.of(
                "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 4475 0,25\n",
                "4925ac98-833b-454b-9342-13ed3dfd3ccf 408 1,00\n"
        )){};
        List<String> expectedIllegitimateLines = new ArrayList<>(){};

        // Act
        List<Player> players = betProcessor.collectData();
        betProcessor.analyseData(players);

        // Assert
        assertEquals(expectedLegitimateLines, betProcessor.getLegitimateLines());
        assertEquals(expectedIllegitimateLines, betProcessor.getIllegitimateLines());
    }
}
