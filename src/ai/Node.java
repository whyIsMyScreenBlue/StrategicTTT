package ai;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Node<Subtype extends Node<Subtype>> {
    /** Node's copy of gs should not be mutated. */
    private final FastGameState gs;
    private double eval;
    // the list of subnodes is in the same order as the list of moves that lead to them
    private List<Subtype> subNodes;

    public Node(FastGameState gs) {
        this.gs = new FastGameState(gs);
        eval = 1 - gs.getCurrentPlayer();
    }

    public Node() {
        gs = new FastGameState();
        eval = 1;
    }

    public abstract Subtype createSubNode(FastGameState gs);

    public double eval() {
        return eval;
    }

    public int bestMove() {
        int bestMove = -1;
        if (gs.isWon()) return bestMove;
        subNodes = gs.playableCoords().stream().map(coords -> createSubNode(new FastGameState(gs).play(coords))).collect(Collectors.toList());
        for (int i = 0; i < gs.playableCoords().size(); i++) {
            double subNodeEval = subNodes.get(i).eval();
            if (betterThan(subNodeEval, eval)) {
                eval = subNodeEval;
                bestMove = gs.playableCoords().get(i);
            }
        }
        return bestMove;
    }

    public boolean betterThan(double eval1, double eval2) {
        if (gs.getCurrentPlayer() == 0) return eval1 < eval2;
        return eval1 > eval2;
    }
}
