package mathis.game;

public enum Direction {

    Up(0),
    Down(1),
    Left(2),
    Right(3);
    private final int value;

    Direction(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }

}
