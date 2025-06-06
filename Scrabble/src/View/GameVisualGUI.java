/*
* Authors: Johnathan Alexander, Erik Picazzo, Andrew Wong, Andrew Huynh
*
* Description: This file contains the code for the GUI representation of
*		modified Scabble. AI was used in the generation of this code,
*		and will be explained throughout the rest of the documentation. 
*/

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
	// Plethora of instance variables used to keep track of a variety of things
    	private Move previewMove = new Move();
    	private JPanel handPanel;

    	private GameBoard board;
    	private ArrayList<GamePiece> selectedForSwap = new ArrayList<>();
    	private final boolean[] voteToEndFlags;
    	private ArrayList<Player> players;
    	private int currentPlayerIndex = 0;
    	private PlayerRecords playerSaves;
    	private boolean isFirstMove = true;

    	private JButton[][] boardButtons;
    	private JTextField letterField, xField, yField;
    	private JButton submitButton;

    	private final int BOARD_SIZE = 15; // assuming standard 15x15 board

    	private ArrayList<Player> showStartupDialog() {

	// Creates the initial window where player's can input their names, 
	// and the number of players participating (up to four)
        JDialog dialog = new JDialog(this, "Scrabble Setup", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(new JLabel("Welcome to Scrabble!"));
        contentPanel.add(Box.createVerticalStrut(10));

	// Handles the drop down box used to select the number of players participating
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countPanel.add(new JLabel("Number of Players:"));
        JComboBox<Integer> playerCountBox = new JComboBox<>(new Integer[]{2, 3, 4});
        countPanel.add(playerCountBox);
        contentPanel.add(countPanel);

	// Handles the display boxes for players to input their names
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

		// When a player has entered their name, it checks the name against the text
		// file containing the saved players and their stats. If that player has played
		// this Scrabble game before, it loads their stats and displays their win/loss
		// ratio. Else, they are noted as a new player. 
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

        // Control buttons for starting the game once all players have input their names, or
	// exiting the application (with "Cancel")
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Start Game");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Visibility control based on player count
	// This handles how many name boxes for players are visible on the GUI.
	// A minimum of two name boxes are always shown, incrementing up to four
	// depending on the number of players selected in the drop down box. 
        playerCountBox.addActionListener(e -> {
            int count = (Integer) playerCountBox.getSelectedItem();
            for (int i = 0; i < 4; i++) {
                nameFields[i].getParent().setVisible(i < count);
            }
            dialog.pack(); // Resize dynamically
        });
        playerCountBox.setSelectedIndex(0); // Trigger once

        ArrayList<Player> players = new ArrayList<>();

	// When the okButton is clicked, creates new players, the number of
	// which depends on the number of players selected to play the game, utilizing
	// the input String names to create the Player objects, and places them in
	// an ArrayList and adding them to the player saves file if they aren't already in it.
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

	// If the cancel button is clicked, the program exits and the window closes.
        cancelButton.addActionListener(l -> {
            players.clear();
            dialog.dispose();
            System.exit(0);
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        return players; // Returns the ArrayList of Player objects. 
    }

    public GameVisualGUI() {
	/*
	* This method creates and displays the main component of the GUI; the GameBoard.
 	* It also displays the player whose turn it currently is and how many points they have, their hand of GamePiece tiles,
  	* and buttons to play their selected word, remove their tiles placed on the board, swap GamePieces
   	* in their hand for new GamePieces from the static store, and button to vote to end the game, where
    	* if all players vote to end the game, the game ends. 
     	*
      	* Additionally, the GUI allows for a 'drag and drop' system of GamePiece placement on the board, 
       	* for an enhanced visual quality and heightened engagement. 
	*/

	// Handles the creation of the GUI window displayed for the GameBoard itself, placing all buttons
	// and establishing the grid with it's various special tiles (colored appropriately), and blank tiles
	// (uncolored). 
    	playerSaves = new PlayerRecords();
    	players = showStartupDialog();
        board = new GameBoard();
        voteToEndFlags = new boolean[players.size()];

	// Sets the window that pops up
        setTitle("Scrabble-Style Game - Visual GUI");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

	// Creates the submit button
        submitButton = new JButton("Play Word");

        // Game board panel (center); created to GameBoard.java file size specifications
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];

	// Creates the grid as a bunch of buttons, coloring the appropriate special tiles to display their uniqueness
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

		// This is from the GUI development phase, when the GUI was being tested with
		// various aspects of the code. It is designed to preview the validity of 
		// individual letter placements, but has since been rendered obsolete by
		// further code developments. We've left it in because the code breaks without it,
		// but have hidden it on the GUI so it cannot be used...
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
        } // End of hidden elements of code.
	    
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
	// This code is also development phase stuff that will break the code if removed. 
	// Thus, it has been left in, and simply not used in the final presentation of the GUI. 
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
    } // End of no longer used code. 


    private void updateBoardDisplay() {
	/*
	* This method updates the board display to account for player moves having been made.
	*/

	// These loops iterate over each element of the grid and pulling any updated values
	// into the visual representation of the board. 
	//
	// Letters that have been placed but not finalized for the player's move are displayed blue,
	// and played letters are switched to black. 
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
	// Sets the current player to the corresponding player at the index in the ArrayList
        Player current = players.get(currentPlayerIndex);
	    
        // Left: Player name + score; displays this information in the bottom left corner of the GUI
        JLabel playerInfo = new JLabel(current.getName() + " | Score: " + current.getPlayerPoints());
        playerInfo.setFont(new Font("SansSerif", Font.BOLD, 16));
        handPanel.add(playerInfo, BorderLayout.WEST);

        // Center: Player's tiles
	// This chunk will display the player's tiles in the bottom center of the GUI.
	// In addition to being selectable and draggable, shift-clicking will allow 
	// a player to highlight a specific tile, which is the precursor to swapping tiles
	// for new GamePieces. 
        JPanel tileContainer = new JPanel(new FlowLayout());
        for (GamePiece piece : current) {
            JButton tileButton = new JButton(piece.getLetter());
            tileButton.setFont(new Font("Monospaced", Font.BOLD, 18));
            tileButton.setTransferHandler(new ValueExportTransferHandler(piece.getLetter()));

            tileButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (e.isShiftDown()) {  // Use Shift-click to select for swap
                        if (selectedForSwap.contains(piece)) {
                            selectedForSwap.remove(piece);
                            tileButton.setBackground(null);  // Un-highlight
                        } else {
                            selectedForSwap.add(piece);
                            tileButton.setBackground(Color.YELLOW);  // Highlight
                        }
                    } else {
                        JComponent comp = (JComponent) e.getSource();
                        TransferHandler handler = comp.getTransferHandler();
                        handler.exportAsDrag(comp, e, TransferHandler.COPY);
                    }
                }
            });

            tileContainer.add(tileButton);
        }

	// Adds the overall tiles display to the GUI
        handPanel.add(tileContainer, BorderLayout.CENTER);

        // Right: Buttons for 'vote to end', 'swap tiles', 'play word', and 'remove letters'
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JButton removeButton = new JButton("Remove Letters");
        removeButton.addActionListener(evt -> {
            Player player = players.get(currentPlayerIndex);

            // Return previewed tiles to the hand
            for (GamePiece piece : previewMove.getMove().values()) {
                current.addToHand(piece);
            }

            previewMove.clear();
            updateBoardDisplay();
        });

	// Players can optionally vote to end the game; this does not affect their play of the game,
	// but when all players have voted to end the game, then the game will end. 
        JButton voteToEndButton = new JButton("Vote to End");
        voteToEndButton.addActionListener(evt -> {
            voteToEndFlags[currentPlayerIndex] = true;
            int count = 0;
            for (boolean b : voteToEndFlags) {
            	if (b==true) count++;
            }
	    // Displays the player that voted to end the game, as well as the total ratio of players that have voted to end the game
            JOptionPane.showMessageDialog(this, current.getName() + " voted to end the game.\n" + 
            							count + "/" +voteToEndFlags.length + " Players have voted.") ;

            // Check if all players have voted to end
            boolean allVoted = true;
            for (boolean voted : voteToEndFlags) {
                if (!voted) {
                    allVoted = false;
                    break;
                }
            }

            if (allVoted) {
                JOptionPane.showMessageDialog(this, "All players voted to end the game. Exiting...");
                SwingUtilities.getWindowAncestor(handPanel).dispose();  // closes the game window
                gameOver(); 
            } else {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                updateBoardDisplay();
            }
        });
	// Adds the vote to end button to the GUI
        buttonPanel.add(voteToEndButton);

        // After pieces have been highlighted, the swap tiles button can be clicked
	// to swap the highlighted tiles with new GamePieces drawn from the static store.
	// A player that has opted to swap tiles has the remainder of their turn skipped. 
        JButton swapButton = new JButton("Swap Tiles");
        swapButton.addActionListener(evt -> {
            Player player = players.get(currentPlayerIndex);

	    // If no tiles have been selected when the swap button is clicked, displays the below message.
            if (selectedForSwap.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select tiles to swap by Shift-clicking them.");
                return;
            }

            player.swap(selectedForSwap);
            selectedForSwap.clear(); // Clear swap selection
            previewMove.clear(); // Don't allow placing after swap
            current.fillPlayerHand();

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // Skip turn
            updateBoardDisplay();
        });
	// Adds the remaining buttons to the GUI
        buttonPanel.add(swapButton);
        buttonPanel.add(submitButton);  // already defined
        buttonPanel.add(removeButton);
        handPanel.add(buttonPanel, BorderLayout.EAST);

        handPanel.revalidate();
        handPanel.repaint();
    }

    private void handleMove(ActionEvent e) {
	/*
	* This method handles player moves based on the tiles they play. 
	*/ 

	// If no tiles have been played, displays this message. 
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

	// Refill the player hand from the static store
        current.fillPlayerHand();
        if (board.checkValidWord(moveToCommit)) {
           currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); 
        }

        // Clear the preview and update display
        previewMove.clear();
        updateBoardDisplay();
    }

    private void gameOver() {
	/*
	* This method handles the events occuring after the game has ended. If one player
 	* has the most points, it displays them as the winner. In the event two players have tied
  	* for points, and there exist other players in the game, they will both be displayed as the 
   	* the winner. Else, if all players in the game have tied, no player will be the winner, and the
    	* tie will be announced. 
     	*
      	* After that display, players have the option to see an overall leaderboard of all players in the
       	* game database, which will display their names, wins, and losses, sorted by wins in descending order
	* and then by losses in ascending order. 
	*/ 

        ArrayList<Player> winners = new ArrayList<Player>();
        int maxPoints = 0;
	// For each player in the list of players, it compares their points to the current max points value.
	// If they have more points, the old winners are cleared and the current player is added to the list
	// of winners. If the current player has the same number of points, then they are simply added to the list.
        for (Player p : players) {
            if (p.getPlayerPoints() > maxPoints) {
                winners.clear();
                maxPoints = p.getPlayerPoints();
                winners.add(p);
            } else if (p.getPlayerPoints() == maxPoints) {
                winners.add(p);
            }
        }

	// Display for when all players tie. 
        if (winners.size() == players.size()) {
            String result = "It's a tie! Everyone has the same score: " + maxPoints + " points.";
            showEndGameDialog(result);
        } 
	// Display for when there is/are definite winners. 
	else {
            for (Player p : players) {
                if (winners.contains(p)) {
                    playerSaves.incrementWins(p.getName());
                } else {
                    playerSaves.incrementLosses(p.getName());
                }
            }

            StringBuilder result = new StringBuilder("🎉 Game Over! Winner(s):\n");
            for (Player p : winners) {
                result.append(p.getName()).append(" with ").append(p.getPlayerPoints()).append(" points\n");
            }
            result.append("\nUpdated Win/Loss Records:\n");
            for (Player p : players) {
                int wins = playerSaves.getWins(p.getName());
                int losses = playerSaves.getLosses(p.getName());
                result.append(p.getName()).append(" - Wins: ").append(wins).append(", Losses: ").append(losses).append("\n");
            }

	    // Updates each players win/loss data in the player saves file.
            playerSaves.writePlayerSaves();
            showEndGameDialog(result.toString()); // Displays the end game dialogue. 
        }

	// Updates the overall save data. 
        playerSaves.writePlayerSaves();
        System.exit(0); // Exits the game
    }

    private void showEndGameDialog(String message) {
	// This method displays the game end dialogue, with options to exit the
	// game immediately after finishing, or to view the leaderboard first. 
        Object[] options = {"OK", "View Leaderboard"};
        int choice = JOptionPane.showOptionDialog(
                this,
                message,
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 1) {
            showLeaderboardDialog();
        }
    }
    
    private void showLeaderboardDialog() {
	// This method displays a window with the leaderboard dialogue, which 
	// contains all data for every player that has played this Scrabble game before. 
        ArrayList<String> names = new ArrayList<>(playerSaves.getPlayerNames());

        // Sort players by wins descending
        names.sort((a, b) -> Integer.compare(playerSaves.getWins(b), playerSaves.getWins(a)));

        // 1. Find the longest player name
        int maxNameLength = "Name".length(); // start with header length
        for (String name : names) {
            if (name.length() > maxNameLength) {
                maxNameLength = name.length();
            }
        }
        maxNameLength += 2; // padding space

        StringBuilder leaderboard = new StringBuilder("🏆 Leaderboard:\n\n");

        // 2. Header
        leaderboard.append(String.format("%-" + maxNameLength + "s %5s %7s\n", "Name", "Wins", "Losses"));
        leaderboard.append(String.format("%-" + maxNameLength + "s %5s %7s\n", "----", "----", "------"));

        // 3. Data rows
        for (String name : names) {
            int wins = playerSaves.getWins(name);
            int losses = playerSaves.getLosses(name);
            leaderboard.append(String.format("%-" + maxNameLength + "s %5d %7d\n", name, wins, losses));
        }

        // 4. Display in monospaced JTextArea
        JTextArea textArea = new JTextArea(leaderboard.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Leaderboard", JOptionPane.PLAIN_MESSAGE);
    }

    private static class ValueExportTransferHandler extends TransferHandler {
	// This handles the dragging and dropping of each player's GamePiece tiles. 	    
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

    // Runs the game. 
    public static void main(String[] args) {
        new GameVisualGUI();
    }
}
