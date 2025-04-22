package View;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GameVisualGUI extends JFrame {
    private GameBoard board;
    private Player p1, p2;
    private boolean playerTurn = false;
    private boolean isFirstMove = true;

    private JButton[][] boardButtons;
    private JTextField wordField, xField, yField;
    private JComboBox<String> directionBox;
    private JTextArea handArea;
    private JButton submitButton;

    private final int BOARD_SIZE = 15; // assuming standard 15x15 board

    public GameVisualGUI() {
        board = new GameBoard();
        p1 = new Player("Johnathan Alexander");
        p2 = new Player("Erik Picazzo");

        setTitle("Scrabble-Style Game - Visual GUI");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel();
        wordField = new JTextField(10);
        xField = new JTextField(3);
        yField = new JTextField(3);
        directionBox = new JComboBox<>(new String[]{"horizontal", "vertical"});
        submitButton = new JButton("Play Word");

        inputPanel.add(new JLabel("Word:"));
        inputPanel.add(wordField);
        inputPanel.add(new JLabel("X (1-15):"));
        inputPanel.add(xField);
        inputPanel.add(new JLabel("Y (1-15):"));
        inputPanel.add(yField);
        inputPanel.add(new JLabel("Direction:"));
        inputPanel.add(directionBox);
        inputPanel.add(submitButton);
        add(inputPanel, BorderLayout.NORTH);

        // Game board panel (center)
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                JButton btn = new JButton("");

                Tile tile = board.getTile(row, col);
                Tile.Type type = tile.getSpecial();
                if (type != null) {
                    switch (type) {
                        case TripleWord -> btn.setBackground(new Color(255, 102, 102)); // red
                        case DoubleWord -> btn.setBackground(new Color(255, 178, 102)); // orange
                        case TripleLetter -> btn.setBackground(new Color(102, 178, 255)); // blue
                        case DoubleLetter -> btn.setBackground(new Color(153, 204, 255)); // light blue
                        case Center -> {
                            btn.setBackground(new Color(255, 255, 153)); // yellow
                            btn.setText("*");
                        }
                        default -> btn.setBackground(Color.WHITE);
                    }


                }
                btn.setOpaque(true);
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                btn.setFont(new Font("SansSerif", Font.BOLD, 14));
                boardButtons[row][col] = btn;
                boardPanel.add(btn);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Player hand panel (bottom)
        handArea = new JTextArea(3, 40);
        handArea.setEditable(false);
        handArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(handArea), BorderLayout.SOUTH);

        // Action listener
        submitButton.addActionListener(this::handleMove);

        updateBoardDisplay();
        setVisible(true);
    }

    private void updateBoardDisplay() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                GamePiece piece = board.getTile(row, col).getPiece();
                String letter = piece != null ? piece.getLetter() : "";
                boardButtons[row][col].setText(letter);
            }
        }

        Player current = playerTurn ? p2 : p1;
        handArea.setText(current.getName() + "'s Hand: " + current.getPlayerHand());
    }

    private void handleMove(ActionEvent e) {
        String word = wordField.getText().toUpperCase().trim();
        int x, y;
        try {
            x = Integer.parseInt(xField.getText()) - 1;
            y = Integer.parseInt(yField.getText()) - 1;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid coordinates! Must be numbers 1–15.");
            return;
        }

        if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE) {
            JOptionPane.showMessageDialog(this, "Coordinates out of bounds!");
            return;
        }

        Move.Directions direction = directionBox.getSelectedItem().equals("horizontal") ?
                Move.Directions.Horizontal : Move.Directions.Vertical;

        Player current = playerTurn ? p2 : p1;
        Move playerMove = new Move();
        playerMove.setPlacement(x, y, direction);

        ArrayList<GamePiece> usedLetters = new ArrayList<>();
        int originalX = x, originalY = y;
        int invalidMove = 0;

        for (String s : word.split("")) {
            GamePiece g = current.getPiece(s);
            if (g != null) {
                playerMove.addPiece(g, x, y);
                usedLetters.add(g);
            } else {
                GamePiece boardPiece = board.getTile(y, x).getPiece();
                if (boardPiece != null && boardPiece.getLetter().equals(s)) {
                    playerMove.addPiece(boardPiece, x, y);
                    for (GamePiece n : usedLetters) {
                        if (n.getLetter().equals(boardPiece.getLetter())) {
                            current.addToHand(n);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "You don't have the letter '" + s + "'");
                    invalidMove = 1;
                    playerMove.clear();
                    for (GamePiece gp : usedLetters) current.addToHand(gp);
                    break;
                }
            }

            if (direction == Move.Directions.Horizontal) x++;
            else y++;

            if (x >= BOARD_SIZE || y >= BOARD_SIZE) {
                JOptionPane.showMessageDialog(this, "Word runs off the board!");
                return;
            }
        }

        if (invalidMove == 0) {
            board.playerMove(playerMove, isFirstMove);
            isFirstMove = false;
            current.fillPlayerHand();
            playerTurn = !playerTurn;
        }

        updateBoardDisplay();
    }

    public static void main(String[] args) {
        new GameVisualGUI();
    }
}
