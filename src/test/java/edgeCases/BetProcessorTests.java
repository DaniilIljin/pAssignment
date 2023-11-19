package edgeCases;

import helpers.Helper;
import main.BetProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    /*TODO:
       test if illegitimate move is bet,
       test if
     */
}
