package ai;

import logic.Coords;

import java.util.ArrayList;
import java.util.List;

public class FastGameState {
    public static final int FREE_MOVE = 9;

    private boolean won;
    private int currentPlayer;
    private int currentSector;
    private int mainBitBoard;
    private final int[] sectorBitBoards;

    public FastGameState() {
        this.won = false;
        this.sectorBitBoards = new int[9];
        this.currentPlayer = 0; // p1
        this.currentSector = FREE_MOVE; // Any
    }

    public FastGameState(FastGameState other) {
        this.mainBitBoard = other.mainBitBoard;
        this.sectorBitBoards = other.sectorBitBoards;
        this.currentPlayer = other.currentPlayer;
        this.currentSector = other.currentSector;
    }

    public boolean isWon() {
        return won;
    }

    public int winner() {
        return (mainBitBoard & Updater.WINNER_MASK) >> 18;
    }

    public void play(int doubleCoords) {
        int sectorCoords = doubleCoords / 9;
        int cellCoords = doubleCoords % 9;
        int sector = sectorBitBoards[sectorCoords] = Updater.update(sectorBitBoards[sectorCoords], cellCoords, currentPlayer);
        if (Updater.sectorIsWon(sector)) {
            mainBitBoard = Updater.update(mainBitBoard, sectorCoords, currentPlayer);
            if (Updater.sectorIsWon(mainBitBoard)) won = true;
        }
        currentPlayer = 1 - currentPlayer;
        currentSector = cellCoords;
        if (Updater.sectorIsWon(sectorBitBoards[cellCoords])) currentSector = FREE_MOVE;
    }

    private List<Integer> sectorPlayableCoords(int sectorCoords) {
        List<Integer> result = new ArrayList<>(9);
        int sector = sectorBitBoards[sectorCoords];
        if (Updater.sectorIsWon(sector)) return result;
        int occupiedCells = ((sector >> 9) | sector); // includes some junk at the beginning
        for (int i = 0; i < 9; i++) {
            if ((occupiedCells & (1 << i)) == 0) result.add(i + 9*sectorCoords); // Return in doubleCoords format (0 to 80)
        }
        return result;
    }

    public List<Integer> playableCoords() {
        List<Integer> result = new ArrayList<>(81);
        if (currentSector == FREE_MOVE) {
            for (int i = 0; i < 9; i++) {
                result.addAll(sectorPlayableCoords(i));
            }
            return result;
        }
        return sectorPlayableCoords(currentSector);
    }
}
