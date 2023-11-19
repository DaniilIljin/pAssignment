package exceptions;

import helpers.Helper;
import main.BetProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionsTests {
    @Test
    public void givenFaultyInputFiles_whenCreateMatchesMethodCalled_thenBetProcessorThrowsNotEnoughDataException() {
        // Arrange
        BetProcessor betProcessor = new BetProcessor(
                "src/test/resources/faulty1_match_data.txt",
                "src/test/resources/players_data.txt",
                "src/test/java/exceptions/result.txt");

        // Assert
        assertThrows(NotEnoughDataException.class, betProcessor::createMatchesMap);
    }

    @Test
    public void givenFaultyInputFiles_whenCreateMatchesMethodCalled_thenBetProcessorThrowsNotAbleToParseDataException() {
        // Arrange
        BetProcessor betProcessor = new BetProcessor(
                "src/test/resources/faulty2_match_data.txt",
                "src/test/resources/players_data.txt",
                "src/test/java/exceptions/result.txt");

        // Assert
        assertThrows(NotAbleToPaseDataException.class, betProcessor::createMatchesMap);
    }

    @Test
    public void givenFaultyInputFiles_whenCreatePlayersMethodCalled_thenBetProcessorThrowsNotAbleToParseDataException() {
        // Arrange
        BetProcessor betProcessor = new BetProcessor(
                "src/test/resources/match_data.txt",
                "src/test/resources/faulty1_player_data.txt",
                "src/test/java/exceptions/result.txt");

        // Assert
        assertThrows(NotAbleToPaseDataException.class, betProcessor::collectData);
    }

    @Test
    public void givenFaultyInputFiles_whenCreatePlayersMethodCalled_thenBetProcessorThrowsNotExistingObjectException() {
        // Arrange
        BetProcessor betProcessor = new BetProcessor(
                "src/test/resources/match_data.txt",
                "src/test/resources/faulty2_player_data.txt",
                "src/test/java/exceptions/result.txt");

        // Assert
        assertThrows(NotExistingObjectException.class, betProcessor::collectData);
    }

    @Test
    public void givenFaultyInputMatchType_whenFindMatchResultTypeMethodCalled_thenHelperThrowsNotExistingTypeException() {
        //Arrange
        String type = "No one won";

        // Assert
        assertThrows(NotExistingTypeException.class, () -> {
            Helper.findMatchResultType(type);
        });
    }
}
