package logic;

/**
 * Anything that can contain 9 subsections. Both the main board and its subboards belong to this category.
 */
public abstract class AbstractBoard implements Winnable {
    public static final int[] WIN_MASKS = new int[] {
            0b111000000,
            0b000111000,
            0b000000111,
            0b100100100,
            0b010010010,
            0b001001001,
            0b100010001,
            0b001010100
    };

    private final Tileable[] tiles;
    /** Bits 0-8 (0 being the units) are X's bitBoard, and bits 9-17 are O's bitboard. */
    private int bitBoards;
    private Player winner;

    public AbstractBoard() {
        tiles = new Tileable[9];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = makeTile();
        }
        bitBoards = 0;
    }

    /** factory method for constructor */
    protected abstract Tileable makeTile();

    /** Getter for {@code tiles} */
    protected Tileable getTile(Coords coords) {
        return tiles[coords.ordinal()];
    }

    public boolean isWon() {
        return winner != null;
    }

    /** Check if any player has won. If so, update and return winner. */
    protected Player checkWinner() {
        for (int winMask : WIN_MASKS) {
            int bitBoardX = bitBoards % 0b1000000000;
            int bitBoardO = bitBoards / 0b1000000000;
            if ((bitBoardX & winMask) == winMask) {
                return winner = Player.X;
            }
            if ((bitBoardO & winMask) == winMask) {
                return winner = Player.O;
            }
        }
        return null;
    }

    /** Check if the specific player has won. This runs a bit faster. */
    protected void checkWinner(Player p) {
        int bitBoard = bitBoards >> (p.ordinal() * 9);
        for (int winMask : WIN_MASKS) {
            if ((bitBoard & winMask) == winMask) {
                winner = p;
                return;
            }
        }
    }

    protected void updateBitBoardAndCheckWinner(Coords coords, Player p) {
        int index = coords.ordinal() + 9*p.ordinal();
        int bitMask = 1 << index;
        bitBoards |= bitMask;
        checkWinner(p);
    }

    /** Getter for {@code winner} */
    public Player getWinner() {
        return winner;
    }

    @Override
    public Player winner() {
        return getWinner();
    }
}
