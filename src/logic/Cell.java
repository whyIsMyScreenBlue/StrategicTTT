package logic;

/**
 * Unit of space which can contain X or O.
 */
public class Cell implements Tileable {
    private Player player;

    public Cell() {
        player = null;
    }

    public Cell(Cell other) {
        player = other.player;
    }

    @Override
    public Player winner() {
        return player;
    }

    public Player place(Player newOwner) {
        if (player == null) {
            player = newOwner;
        }
        throw new RuntimeException("Cell occupied");
    }
}
