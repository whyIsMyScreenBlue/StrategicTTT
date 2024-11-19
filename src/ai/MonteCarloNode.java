package ai;

import java.util.List;
import java.util.Random;

public class MonteCarloNode extends Node<MonteCarloNode> {
    public static final Random RNG = new Random();
    public static final long PLAYOFF_COUNT = 10000;

    private final FastGameState gs;
    private List<Integer> options;
    private double score;
    private long playoffs;

    public MonteCarloNode(FastGameState gs) {
        super(gs);
        this.gs = new FastGameState(gs);
    }

    @Override
    public MonteCarloNode createSubNode(FastGameState gs) {
        return new MonteCarloNode(gs);
    }

    // Performance critical
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

    @Override
    public double eval() {
        doPlayOffs(PLAYOFF_COUNT);
        return score / playoffs;
    }
}
