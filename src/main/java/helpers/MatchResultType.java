package helpers;

public enum MatchResultType {
    A("A"),
    B("B"),
    DRAW("DRAW");
    private final String stringValue;

    MatchResultType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
