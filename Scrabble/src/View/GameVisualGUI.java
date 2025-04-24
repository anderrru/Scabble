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
import java.util.ArrayList;

public class GameVisualGUI extends JFrame {
    private Move previewMove = new Move();
    private JPanel handPanel;

    private GameBoard board;
    private ArrayList<Player> players;
    private int currentPlayerIndex = 0;
    private PlayerRecords playerSaves;
    private boolean isFirstMove = true;

    private JButton[][] boardButtons;
    private JTextField letterField, xField, yField;
    private JButton submitButton;

    private final int BOARD_SIZE = 15; // assuming standard 15x15 board

    private ArrayList<Player> showStartupDialog() {
    	  
        JDialog dialog = new JDialog(this, "Scrabble Setup", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(new JLabel("Welcome to Scrabble!"));
        contentPanel.add(Box.createVerticalStrut(10));

        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countPanel.add(new JLabel("Number of Players:"));
        JComboBox<Integer> playerCountBox = new JComboBox<>(new Integer[]{2, 3, 4});
        countPanel.add(playerCountBox);
        contentPanel.add(countPanel);

        JPanel namesPanel = new JPanel();
        namesPanel.setLayout(new BoxLayout(namesPanel, BoxLayout.Y_AXIS));
        JTextField[] nameFields = new JTextField[4];
        JLabel[] recordLabels = new JLabel[4];

		for (int i = 0; i < 4; i++) {
		    JPanel playerPanel = new JPanel();
		    playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
		    JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		    inputRow.add(new JLabel("Name for Player " + (i + 1) + ":"));
		    nameFields[i] = new JTextField("Player " + (i + 1), 15);
		    inputRow.add(nameFields[i]);
		
		    recordLabels[i] = new JLabel(" ");
		    recordLabels[i].setFont(new Font("SansSerif", Font.ITALIC, 12));
		    recordLabels[i].setForeground(Color.DARK_GRAY);
		
		    playerPanel.add(inputRow);
		    playerPanel.add(recordLabels[i]);
		    namesPanel.add(playerPanel);
		
		    final int index = i;
		    nameFields[i].addActionListener(evt -> {
		        String name = nameFields[index].getText().trim();
		        if (playerSaves.getPlayerNames().contains(name)) {
			        int wins = playerSaves.getWins(name);
			        int losses = playerSaves.getLosses(name);
			        recordLabels[index].setText("W/L: " + wins + " / " + losses);
		        }
		        else {
		        	recordLabels[index].setText("New Player!");;
		        }
		
		        recordLabels[index].revalidate();
		        recordLabels[index].repaint();
		    });
		}

        contentPanel.add(namesPanel);

        // Control buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Start Game");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Visibility control based on player count
        playerCountBox.addActionListener(e -> {
            int count = (Integer) playerCountBox.getSelectedItem();
            for (int i = 0; i < 4; i++) {
                nameFields[i].getParent().setVisible(i < count);
            }
            dialog.pack(); // Resize dynamically
        });
        playerCountBox.setSelectedIndex(0); // Trigger once

        ArrayList<Player> players = new ArrayList<>();

        okButton.addActionListener(e -> {
            int count = (Integer) playerCountBox.getSelectedItem();
            for (int i = 0; i < count; i++) {
                String name = nameFields[i].getText().trim();
                if (name.isEmpty()) name = "Player " + (i + 1);
                players.add(new Player(name));
                playerSaves.addPlayer(name);
            }
            dialog.dispose();
        });

        cancelButton.addActionListener(l -> {
            players.clear();
            dialog.dispose();
            System.exit(0);
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        playerSaves.writePlayerSaves();
        return players;
        
    }

    
    public GameVisualGUI() {
    	playerSaves = new PlayerRecords();
    	players = showStartupDialog();
        board = new GameBoard();

        setTitle("Scrabble-Style Game - Visual GUI");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        submitButton = new JButton("Play Word");

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
                            Player current = players.get(currentPlayerIndex);
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
        handPanel = new JPanel(new BorderLayout(10, 0));
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

        Player current = players.get(currentPlayerIndex);
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
        Player current = players.get(currentPlayerIndex);

        // Left: Player name + score
        JLabel playerInfo = new JLabel(current.getName() + " | Score: " + current.getPlayerPoints());
        playerInfo.setFont(new Font("SansSerif", Font.BOLD, 16));
        handPanel.add(playerInfo, BorderLayout.WEST);

        // Center: Player's tiles
        JPanel tileContainer = new JPanel(new FlowLayout());
        for (GamePiece piece : current) {
            JButton tileButton = new JButton(piece.getLetter());
            tileButton.setFont(new Font("Monospaced", Font.BOLD, 18));
            tileButton.setTransferHandler(new ValueExportTransferHandler(piece.getLetter()));

            tileButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    JComponent comp = (JComponent) e.getSource();
                    TransferHandler handler = comp.getTransferHandler();
                    handler.exportAsDrag(comp, e, TransferHandler.COPY);
                }
            });

            tileContainer.add(tileButton);
        }
        handPanel.add(tileContainer, BorderLayout.CENTER);

        // Right: Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JButton removeButton = new JButton("Remove Letters");
        removeButton.addActionListener(evt -> {
            previewMove.clear();
            updateBoardDisplay();
        });

        buttonPanel.add(submitButton);  // already defined
        buttonPanel.add(removeButton);
        handPanel.add(buttonPanel, BorderLayout.EAST);

        handPanel.revalidate();
        handPanel.repaint();


    }

    private void handleMove(ActionEvent e) {
        if (previewMove.getMove().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No previewed letters to submit!");
            return;
        }

        Player current = players.get(currentPlayerIndex);

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
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

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
