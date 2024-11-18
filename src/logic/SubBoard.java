package logic;

public class SubBoard extends AbstractBoard implements Tileable {

    @Override
    protected Tileable makeTile() {
        return new Cell();
    }

    @Override
    protected Cell getTile(Coords coords) {
        return (Cell) super.getTile(coords);
    }

    protected Coords play(Coords cell, Player p) {
        getTile(cell).place(p);
        updateBitBoardAndCheckWinner(cell, p);
        return cell;
    }

    @Override
    public Player winner() {
        return null;
    }
}
