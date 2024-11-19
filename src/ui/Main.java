package ui;

import ai.FastGameState;
import ai.MonteCarloNode;
import logic.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class Main {
    private static FastGameState gs = new FastGameState();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Strategic Tic-Tac-Toe (local)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the main grid panel with a grid layout
        JPanel gridPanel = new JPanel(new GridLayout(3, 3)); // 3x3 grid for the subgrids (3x3 buttons per subgrid)
        JButton[][] buttons = new JButton[9][9];

        JLabel playerLabel = new JLabel("Current Player:");
        JTextField playerField = new JTextField("Player 1");
        playerField.setEditable(false);

        JLabel sectorLabel = new JLabel("Current Sector:");
        JTextField sectorField = new JTextField("Any");
        sectorField.setEditable(false);

        // Create 9 subgrid panels (each 3x3 grid of buttons)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JPanel subgridPanel = new JPanel(new GridLayout(3, 3)); // 3x3 grid for buttons inside each subgrid
                subgridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5)); // Thicker border between subgrids

                // Add buttons inside each subgrid
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int buttonRow = row * 3 + i;
                        int buttonCol = col * 3 + j;
                        buttons[buttonRow][buttonCol] = new JButton("");
                        buttons[buttonRow][buttonCol].setFont(new Font("Arial", Font.BOLD, 20));
                        buttons[buttonRow][buttonCol].addActionListener(initButton(27*(buttonRow/3) + 9*(buttonCol/3) + 3*(buttonRow%3) + buttonCol%3,
                                playerField, sectorField, buttons));
                        subgridPanel.add(buttons[buttonRow][buttonCol]);
                    }
                }

                // Add the subgrid panel to the main grid panel
                gridPanel.add(subgridPanel);
            }
        }

        // Side panel for player information
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        sidePanel.add(playerLabel);
        sidePanel.add(playerField);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(sectorLabel);
        sidePanel.add(sectorField);

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(sidePanel, BorderLayout.EAST);

        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public static void refreshFrame(JFrame frame) {
        frame.revalidate(); // Recalculates layout
        frame.repaint();    // Redraws the frame
    }

    private static ActionListener initButton(int doubleCoords, JTextField playerField, JTextField sectorField, JButton[][] buttons) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                if (gs.isWon()) System.out.println("Game over");
                else if (gs.playableCoords().contains(doubleCoords)) {
                    button.setText(new String[] {"X", "O"} [gs.getCurrentPlayer()]);
                    gs.play(doubleCoords);
                    optionalAIMove("MCTS", buttons);
                    playerField.setText("Player " + (gs.getCurrentPlayer() + 1));
                    sectorField.setText(new String[]{"NW", "N", "NE", "W", "C", "E", "SW", "S", "SE", "Any"}[gs.getCurrentSector()]);
                }
                else System.out.println("Can't play here");
            }
        };
    }

    public static FastGameState getGS() {
        return gs;
    }

    private static void optionalAIMove(String aiType, JButton[][] buttons) {
        if (aiType.equals("MCTS")) {
            MonteCarloNode node = new MonteCarloNode(gs);
            int coords = node.bestMove();
            int sectorCoords = coords / 9;
            int cellCoords = coords % 9;
            buttons[3 * (sectorCoords / 3) + cellCoords / 3][3 * (sectorCoords % 3) + cellCoords % 3].setText(new String[] {"X", "O"} [gs.getCurrentPlayer()]);
            gs.play(coords);
            System.out.println(gs);
        }
    }
}
