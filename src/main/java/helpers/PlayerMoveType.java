package helpers;

public enum PlayerMoveType {
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW"),
    BET("BET");

    PlayerMoveType(String stringValue) {
        this.stringValue = stringValue;
    }
    private final String stringValue;

    public String getStringValue() {
        return stringValue;
    }
}
