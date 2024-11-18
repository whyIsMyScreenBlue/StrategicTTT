package logic;

public class Board extends AbstractBoard {

    @Override
    protected Tileable makeTile() {
        return new SubBoard();
    }

    @Override
    protected SubBoard getTile(Coords coords) {
        return (SubBoard) super.getTile(coords);
    }

    /** Plays on specified cell, and returns the coords of the sector the next player goes to (if still open) */
    public Coords play(Coords sector, Coords cell, Player p) {
        SubBoard subBoard = getTile(sector);
        Coords next = subBoard.play(cell, p);
        if (subBoard.isWon()) updateBitBoardAndCheckWinner(sector, p);
        return next;
    }
}
