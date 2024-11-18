package logic;

public enum Coords {
    NW, N, NE, W, C, E, SW, S, SE;

    public static final int BOARD_LENGTH = 3;

    public int getX() {
        return ordinal() % 3;
    }
    public int getY() {
        return ordinal() / 3;
    }
    public int getCoords(int x, int y) {
        if (x < 0 || x >= BOARD_LENGTH || y < 0 || y >= BOARD_LENGTH) throw new IllegalArgumentException();
        return x + 3*y;
    }
    public boolean inDiagonal() {
        return getX() + getY() % 2 == 0;
    }
}
