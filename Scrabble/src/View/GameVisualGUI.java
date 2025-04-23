package View;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameVisualGUI extends JFrame {
    private Move previewMove = new Move();
    private JButton previewButton;
    private JPanel handPanel;

    private GameBoard board;
    private Player p1, p2;
    private boolean playerTurn = false;
    private boolean isFirstMove = true;

    private JButton[][] boardButtons;
    private JTextField letterField, xField, yField;
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
        letterField = new JTextField(10);
        xField = new JTextField(3);
        yField = new JTextField(3);
        submitButton = new JButton("Play Word");
        previewButton = new JButton("Preview Letter");
        inputPanel.add(previewButton);
        previewButton.addActionListener(e -> previewLetter());


        inputPanel.add(new JLabel("Letter:"));
        inputPanel.add(letterField);
        inputPanel.add(new JLabel("X (1-15):"));
        inputPanel.add(xField);
        inputPanel.add(new JLabel("Y (1-15):"));
        inputPanel.add(yField);
        inputPanel.add(new JLabel("Direction:"));
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
                int finalRow = row;
                int finalCol = col;
                btn.setTransferHandler(new TransferHandler("text") {
                    public boolean canImport(TransferHandler.TransferSupport support) {
                        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
                    }

                    public boolean importData(TransferHandler.TransferSupport support) {
                        if (!canImport(support)) return false;

                        try {
                            String letter = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                            btn.setText(letter);
                            btn.setForeground(Color.BLUE);

                            // Optionally store into previewMove
                            Player current = playerTurn ? p2 : p1;
                            GamePiece dragged = current.getPiece(letter);
                            if (dragged != null) {
                                previewMove.addPiece(dragged, finalCol, finalRow);
                            }

                            updateBoardDisplay();
                            return true;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return false;
                        }
                    }
                });

                btn.setOpaque(true);
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                btn.setFont(new Font("SansSerif", Font.BOLD, 14));
                boardButtons[row][col] = btn;
                boardPanel.add(btn);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Player hand panel (bottom)
        handPanel = new JPanel(new FlowLayout());
        add(handPanel, BorderLayout.SOUTH);

        // Action listener
        submitButton.addActionListener(this::handleMove);

        updateBoardDisplay();
        setVisible(true);
    }

    private void previewLetter() {
        String letter = letterField.getText().toUpperCase().trim();
        if (letter.length() != 1 || !Character.isLetter(letter.charAt(0))) {
            JOptionPane.showMessageDialog(this, "Please enter a single letter.");
            return;
        }

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

        Player current = playerTurn ? p2 : p1;
        GamePiece piece = current.getPiece(letter);
        if (piece == null) {
            JOptionPane.showMessageDialog(this, "You don't have the letter '" + letter + "'");
            return;
        }

        // Store it in the preview move
        previewMove.addPiece(piece, x, y);
        updateBoardDisplay();
    }


    private void updateBoardDisplay() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                GamePiece piece = board.getTile(row, col).getPiece();
                String letter = piece != null ? piece.getLetter() : "";
                // Check preview move if board position is empty
                if (piece == null && previewMove != null) {
                    GamePiece previewPiece = previewMove.getAtPosition(row, col);
                    if (previewPiece != null) {
                        letter = previewPiece.getLetter();
                        boardButtons[col][row].setForeground(Color.BLUE); // Make preview letters blue
                    } else {
                        boardButtons[col][row].setForeground(Color.BLACK); // Reset color
                    }
                } else {
                    boardButtons[col][row].setForeground(Color.BLACK); // Reset color
                }

                boardButtons[col][row].setText(letter);
            }
        }

        handPanel.removeAll();
        Player current = playerTurn ? p2 : p1;

        for (GamePiece piece : current) {
            JButton tileButton = new JButton(piece.getLetter());
            tileButton.setFont(new Font("Monospaced", Font.BOLD, 18));
            tileButton.setTransferHandler(new ValueExportTransferHandler(piece.getLetter())); // custom class below

            tileButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    JComponent comp = (JComponent) e.getSource();
                    TransferHandler handler = comp.getTransferHandler();
                    handler.exportAsDrag(comp, e, TransferHandler.COPY);
                }
            });

            handPanel.add(tileButton);
        }
        handPanel.revalidate();
        handPanel.repaint();

    }

    private void handleMove(ActionEvent e) {
        if (previewMove.getMove().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No previewed letters to submit!");
            return;
        }

        Player current = playerTurn ? p2 : p1;

        // Create a new Move using the preview
        Move moveToCommit = new Move();
        for (Position pos : previewMove.getMove().keySet()) {
            GamePiece piece = previewMove.getMove().get(pos);
            moveToCommit.addPiece(piece, pos.getX(), pos.getY());
        }

        // Set placement using start position and detected direction
        Move.Directions direction = moveToCommit.getDirection();
        if (direction == null && moveToCommit.getMove().size() > 1) {
            JOptionPane.showMessageDialog(this, "Invalid move: tiles must be in a straight line.");
            return;
        }
        Position start = moveToCommit.getStartPosition();

        // Commit the move to the board
        board.playerMove(moveToCommit, isFirstMove, current);
        isFirstMove = false;

        current.fillPlayerHand();
        playerTurn = !playerTurn;

        // Clear the preview and update display
        previewMove.clear();
        updateBoardDisplay();
    }

    private static class ValueExportTransferHandler extends TransferHandler {
        private final String value;

        public ValueExportTransferHandler(String value) {
            this.value = value;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new StringSelection(value);
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY;
        }
    }

    public static void main(String[] args) {
        new GameVisualGUI();
    }
}
