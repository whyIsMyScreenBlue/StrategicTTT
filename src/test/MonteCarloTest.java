package test;

import ai.FastGameState;
import ai.MonteCarloNode;
import ai.Updater;

public class MonteCarloTest {
    public static void main(String[] args) {
        MonteCarloNode mct = new MonteCarloNode(new FastGameState());
        mct.doPlayOffs(1000000);
        System.out.println(mct.eval());
    }
}
