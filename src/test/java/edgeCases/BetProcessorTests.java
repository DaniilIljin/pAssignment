package edgeCases;

import helpers.Helper;
import main.BetProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BetProcessorTests {

    @Test
    public void givenEmptyInputFiles_whenRunMethodCalled_thenResultFileContainsOnlyZero() {
        // Arrange
        BetProcessor betProcessor = new BetProcessor(
                "src/test/resources/empty_match_data.txt",
                "src/test/resources/empty_players_data.txt",
                "src/test/java/edgeCases/result.txt");

        String expectedContent = "0";

        // Act
        betProcessor.run();
        String actualContent = String.join(
                "", Helper.readFromFile("src/test/java/edgeCases/result.txt")).trim();

        // Assert
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void givenInputFiles_whenRunMethodCalled_thenResultFileContainsIllegalBet() {
        // Arrange
        BetProcessor betProcessor = new BetProcessor(
                "src/test/resources/match_data.txt",
                "src/test/resources/illegal_bet_player_data.txt",
                "src/test/java/edgeCases/result.txt");

        String expectedIllegalBetLine = "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 BET abae2255-4255-4304-8589-737cdff61640 500 A";

        // Act
        betProcessor.run();
        String actualContent = String.join("", Helper.readFromFile("src/test/java/edgeCases/result.txt"));

        // Assert
        assertTrue(actualContent.contains(expectedIllegalBetLine));
    }
}
