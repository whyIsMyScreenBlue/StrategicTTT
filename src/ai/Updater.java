package ai;

import logic.Player;

/**
 * Static method class to handle integers in a bitboard layout. Bits 0-8 are p1's bitboard, Bits 9-17 are p2's bitboard,
 * Bit 18 is the winner, Bit 19 is whether there is a winner.
 * */
public final class Updater {
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

    public static final int WINNER_MASK = 1 << 18;
    public static final int IS_WON_MASK = 1 << 19;
    public static final int BOARD_MASK = 0b111111111;

    private Updater() {}

    public static int update(int bitBoards, int coords, int player) {
        int index = coords + 9*player;
        bitBoards |= 1 << index; // Occupy specified coords.
        int playerBitBoard = bitBoards >> (player * 9);
        for (int winMask : WIN_MASKS) {
            if ((playerBitBoard & winMask) == winMask) {
                bitBoards |= IS_WON_MASK; // set outcome to won
                bitBoards |= player * WINNER_MASK; // set winner to player
                return bitBoards;
            }
        }
        return bitBoards;
    }

    public static boolean sectorIsWon(int bitBoards) {
        return (bitBoards & Updater.IS_WON_MASK) == Updater.IS_WON_MASK;
    }
}
