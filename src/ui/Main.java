package ui;

import ai.FastGameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class Main {
    private static FastGameState gs = new FastGameState();

    private static String currentPlayer = "Player 1";
    private static String currentSector = "Any";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("9x9 Grid with Player Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        JButton[][] buttons = new JButton[9][9];

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                buttons[row][col].addActionListener(new ButtonClickListener());
                gridPanel.add(buttons[row][col]);
            }
        }

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        JLabel playerLabel = new JLabel("Current Player:");
        JTextField playerField = new JTextField(currentPlayer);
        playerField.setEditable(false);

        JLabel sectorLabel = new JLabel("Current Sector:");
        JTextField sectorField = new JTextField(currentSector);
        sectorField.setEditable(false);

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

    private static class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("X")) {
                button.setText("O");
            } else {
                button.setText("X");
            }
            currentPlayer += 1;
        }
    }
}
