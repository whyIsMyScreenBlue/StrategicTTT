package ai;

import java.util.List;
import java.util.Random;

public class MonteCarloNode {
    public static final Random RNG = new Random();

    private final FastGameState gs;
    private List<Integer> options;
    private double score;
    private long playoffs;

    public MonteCarloNode(FastGameState gs) {
        this.gs = gs;
    }

    public void doPlayOffs(long n) {
        for (long i = 0; i < n; i++) {
            playoffs += 1;
            FastGameState playOffGS = new FastGameState(gs);
            while (true) {
                if (playOffGS.isWon()) {
                    score += playOffGS.winner();
                    break;
                }
                List<Integer> options = playOffGS.playableCoords();
                if (options.isEmpty()) { // draw
                    score += 0.5;
                    break;
                }
                int rand = RNG.nextInt(options.size());
                playOffGS.play(options.get(rand));
            }
        }
    }

    public double eval() {
        return score / playoffs;
    }
}
