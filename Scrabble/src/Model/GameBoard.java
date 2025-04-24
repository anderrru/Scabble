/*
* Authors: Johnathan Alexander, Erik PIcazzo, Andrew Wong, Andrew Huynh
*
* Description: This file initializes the standard Scrabble game board,
* creating a 15 x 15 space of empty Tile objects for players to form
* various, legal Scrabble words on. In the same manner as the official
* Scrabble board, some spaces contain double and triple, letter and word
* scores for additional points bonuses. In addition to methods that form
* and check words, this method has a toString method to print a representation
* of the board to the console, so that the game can be played without a
* GUI. 
* 
*/

package Model;

import java.util.Arrays;
import java.util.HashMap;

import Model.Tile.Type;

public class GameBoard {
	private Tile[][] board;
	private WordMap wordMap;

	// Constructor fills the board with new Tile objects
	public GameBoard() {
		board = new Tile[15][15];
		for (int i = 0; i<board.length; i++) {
			for (int j = 0; j<board.length; j++) {
				board[i][j] = new Tile();
			}
		}
		setSpecialTiles();
		wordMap = new WordMap(); // HashMap of all Scrabble-legal words and their hash code values

	}

	private void setSpecialTiles() {
		/*
		* This method updates all tiles that provide letter/word multiplier
  		* point values in accordance to the official Scrabble game board to
    		* contain necessary indicator of their special tile status. 
		*/
		int[][] tripleWordCoords = {
				{0, 0}, {0, 7}, {0, 14},
				{7, 0}, {7, 14},
				{14, 0}, {14, 7}, {14, 14}
		};

		int[][] tripleLetterCoords = {
				{1, 5}, {1, 9},
				{5, 1}, {5, 5}, {5, 9}, {5, 13},
				{9, 1}, {9, 5}, {9, 9}, {9, 13},
				{13, 5}, {13, 9}
		};

		int[][] doubleWordCoords = {
				{1, 1}, {2, 2}, {3, 3}, {4, 4},
				{1, 13}, {2, 12}, {3, 11}, {4, 10},
				{10, 10}, {11, 11}, {12, 12}, {13, 13},
				{13, 1}, {12, 2}, {11, 3}, {10, 4}
		};

		int[][] doubleLetterCoords = {
				{0, 3}, {0, 11}, {2, 6}, {2, 8},
				{3, 0}, {3, 7}, {3, 14},
				{6, 2}, {6, 6}, {6, 8}, {6, 12},
				{7, 3}, {7, 11},
				{8, 2}, {8, 6}, {8, 8}, {8, 12},
				{11, 0}, {11, 7}, {11, 14},
				{12, 6}, {12, 8}, {14, 3}, {14, 11}
		};

		board[7][7].setSpecial(Type.Center);

		for (int[] coord : tripleWordCoords) {
			board[coord[0]][coord[1]].setSpecial(Type.TripleWord);
		}

		for (int[] coord : tripleLetterCoords) {
			board[coord[0]][coord[1]].setSpecial(Type.TripleLetter);
		}

		for (int[] coord : doubleWordCoords) {
			board[coord[0]][coord[1]].setSpecial(Type.DoubleWord);
		}

		for (int[] coord : doubleLetterCoords) {
			board[coord[0]][coord[1]].setSpecial(Type.DoubleLetter);
		}
	}
	
	public Tile getTile(int x, int y) {
		// This method returns a Tile at the input x/y coordinates
		return board[y][x];
	}

	public void playerMove(Move move, boolean firstMove, Player player) {
		/*
		* This method makes the player's "official" move, first checking to see if the move is
   		* the first of the game, and then checks the validity of the move based on position
      		* or checks of the move is valid based on position (and other words on the board) and 
	 	* if the word is a legal Scrabble word. 
    		*
       		* If these conditions are met, the place method is called to place the word on the board.
		*/
		if (firstMove && checkValidMoves(move.getMove()) || checkValidMoves(move.getMove()) && checkValidWord(move)) {
			place(move.getMove(), player);
		}
	}

	private boolean checkValidMoves(HashMap<Position, GamePiece> move) {
		/*
		* This method is a helper method for checking the validity of a move based
   		* on the desired position of the move. 
		*/ 
		for (Position pos : move.keySet()) {
			/*
			* If the board at each position is not empty AND the existing letter at that position does not match
    			* the letter the player is trying to put on the board, OR the GamePiece at that position does not match
			* the GamePiece the player is trying to place down AND the Tile at the desired position is not equal 
    			* to null, return false. Else, return true
			*/ 
			if (!board[pos.getY()][pos.getX()].isEmpty() && !board[pos.getY()][pos.getX()].getPiece().getLetter().equals(move.get(pos).getLetter())
				|| (board[pos.getY()][pos.getX()].getPiece() != move.get(pos)) && (board[pos.getY()][pos.getX()].getPiece() != null)) {
				return false;
			}
		}
		return true;
	}

	// helper method to place the gamepieces from move onto board
	private void place(HashMap<Position, GamePiece> move, Player player) {
		/*
		* This method is run after the validity checks, and actually places the desired 
   		* word on the board. It moves through each position/letter combination in the move, 
      		* and sets the corresponding Tile at the position on the board equal to the GamePiece. 
		*
    		* Additionally, it sums the point total from the letter's corresponding point values
       		* taking into consideration double and triple letter word scores at it iterates, and
		* making note of any double and triple word scores it covers as it iterates, updating
     		* the overall score if either of those tiles have been used at the very end. Finally, 
		* it adds the score of the placed word to the player's overall point total. 
   		* Each score multiplier can only be used once. 
		*/
		int pointSum = 0;
		boolean doubleWordScore = false;
		boolean tripleWordScore = false;

		for (Position pos : move.keySet()) {
			board[pos.getY()][pos.getX()].setPiece(move.get(pos));

			// Checks for double letter score
			if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.DoubleLetter) {
				pointSum += move.get(pos).getPointValue() * 2;
			}
			// Checks for triple letter score
			else if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.TripleLetter) {
				pointSum += move.get(pos).getPointValue() * 3;
			}
			else {
				pointSum += move.get(pos).getPointValue();
			}

			// Checks for double word score
			if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.DoubleWord) {
				doubleWordScore = true;
			}
			// Checks for triple word score. 
			else if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.TripleWord) {
				tripleWordScore = true;
			}
		}

		// If double or triple word score has been updated to true, updates the overall point value of the word
		if (doubleWordScore == true) pointSum *= 2;
		if (tripleWordScore == true) pointSum *= 3;

		// Updates the player point total
		player.updatePlayerPoints(pointSum);
	}
		
	private boolean checkValidWord(Move move) {
		// This method checks if a word is Scrabble legal and returns a true/false value accordingly
		return wordMap.getWord(formWord(move).hashCode()) != null;
	}

	private String formWord(Move move) {
		/*
		* This method forms a word based on the placed letters on the board. It takes the
  		* newly placed letters from the player's move and checks to see if those letters
    		* are appended to another word, and builds the String of the full word.
		*/

		// Gets the starting coordinates of the player's move
		Position startWord = getStartOfWord(move.getStartPosition(), move.getDirection());
		int x = startWord.getX();
		int y = startWord.getY();
		StringBuilder word = new StringBuilder();

		// Checks th ensure that the iterated coordinates remain within the bounds of the game board
		while (x >= 0 && x < board[0].length && y >= 0 && y < board.length) {
			// If the x/y position exists within the player move, adds the letter of the GamePiece associated
			// with that move to the String
			if (move.getAtPosition(x, y) != null) {
				word.append(move.getAtPosition(x, y).getLetter());
			} 
			// If the board at the coordinate position contains a GamePiece, adds the letter associated
			// with that GamePiece to the String. 
			else if (board[y][x].getPiece() != null) {
				word.append(board[y][x].getPiece().getLetter());
			} 
			// Otherwise breaks the loop, since the board is empty at that spot and is not a
			// space the player intends to use. 
			else {
				break;
			}

			// Updates the coordinate of the direction the player is trying to put a word in
			// to continue iterating. 
			if (move.getDirection() == Move.Directions.Horizontal) {
				x++;
			} else {
				y++;
			}
		}

		return word.toString();
	}

	private Position getStartOfWord(Position startMove, Move.Directions direction) {
		/*
		* This helper method finds the coordinate position of the start of 
  		* a word on the Scrabble board. If the player has appended letters
    		* to an already existing word, it moves backwards along the board
      		* until it finds the beginning of the word, and returns that position. 
		*/ 
		int x = startMove.getX();
		int y = startMove.getY();

		// Switch statement for directionality
		switch (direction) {
			case Horizontal:
				// Move left until the tile is empty or out of bounds
				while (x > 0 && board[y][x - 1].getPiece() != null) {
					x--;
				}
				break;
			case Vertical:
				// Move up until the tile is empty or out of bounds
				while (y > 0 && board[y - 1][x].getPiece() != null) {
					y--;
				}
				break;
		}
		return new Position(x, y);
	}

	@Override
	public String toString() {
		// This method returns a String representation of the current state of the board.
		String result = "";

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				// Add the string representation of the Tile to the result string
				result += board[i][j].toString();

				// Add column separator except for the last column
				if (j < 15) {
					result += " | ";
				}
			}

			// Add row separator except for the last row
			if (i < board.length - 1) {
				result += "\n" + "-".repeat(board[i].length * 5 - 1) + "\n"; // Create a row of '-' for separator
			} else {
				result += "\n"; // To add a final newline after the last row
			}
		}

		return result;
	}
}
