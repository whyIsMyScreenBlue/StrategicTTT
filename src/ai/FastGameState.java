package ai;

import logic.Coords;

import java.util.ArrayList;
import java.util.Arrays;
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
        this.sectorBitBoards = Arrays.copyOf(other.sectorBitBoards, 9);
        this.currentPlayer = other.currentPlayer;
        this.currentSector = other.currentSector;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentSector() {
        return currentSector;
    }

    public boolean isWon() {
        return won;
    }

    public int winner() {
        return (mainBitBoard & Updater.WINNER_MASK) >> 18;
    }

    public FastGameState play(int doubleCoords) {
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
        // System.out.println(this);
        return this;
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
        if (currentSector == FREE_MOVE) {
            List<Integer> result = new ArrayList<>(81);
            for (int i = 0; i < 9; i++) {
                result.addAll(sectorPlayableCoords(i));
            }
            return result;
        }
        return sectorPlayableCoords(currentSector);
    }

    @Override
    public String toString() {
        String result = "";
        result += "Current player: " + (currentPlayer + 1) + "\n";
        result += "Current sector: " + ((currentSector == 9)? "any" : currentSector) + "\n";
        for (int k = 0; k < 9; k++) {
            int bitBoard = sectorBitBoards[k];
            for (int i = 0; i < 9; i++) {
                if ((bitBoard & (1 << i)) != 0) result += 'X';
                else if ((bitBoard & (1 << (i + 9))) != 0) result += 'O';
                else result += '-';
            }
            result += " ";
            if ((mainBitBoard & (1 << k)) != 0) result += 'X';
            else if ((mainBitBoard & (1 << (k + 9))) != 0) result += 'O';
            else result += '-';
            result += "\n";
        }
        if (isWon()) result += "won\n";
        return result;
    }
}
