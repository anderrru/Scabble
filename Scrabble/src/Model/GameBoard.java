/*
* Authors: Johnathan Alexander, Erik Picazzo, Andrew Wong, Andrew Huynh
*
* Description: This file initializes the standard Scrabble game board,
* 		creating a 15 x 15 space of empty Tile objects for players to form
* 		various, legal Scrabble words on. In the same manner as the official
* 		Scrabble board, some spaces contain double and triple, letter and word
* 		scores for additional points bonuses. In addition to methods that form
* 		and check words, this method has a toString method to print a representation
* 		of the board to the console, so that the game can be played without a GUI. 
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
  		* Else, the letters are returned to the player's hand
		*/

		if (!firstMove && move.getMove().size() == 1) {
			searchAroundLetter(move);
		}
		if (firstMove && checkValidMoves(move.getMove()) || checkValidMoves(move.getMove()) && checkValidWord(move)) {
			place(move.getMove(), player);
		}
		else {
			for (Position p : move.getMove().keySet()) {
				player.addToHand(move.getAtPosition(p.getX(), p.getY()));
			}
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

	private void place(HashMap<Position, GamePiece> move, Player player) {
		/*
		* This method places a word on the GameBoard using the player's
   		* move (coordinates and directionality).
      		*
		* Additionally, it keeps track of the points earned by the move,
    		* summing each letter's point value. Furthermore, is takes into
       		* account the special tiles, and will boost the point values of
		* the letter and/or overall word accordingly. 
		*/
		int pointSum = 0;
		boolean doubleWordScore = false;
		boolean tripleWordScore = false;

		// Iterates over each position in the move to play each GamePiece
		for (Position pos : move.keySet()) {
			board[pos.getY()][pos.getX()].setPiece(move.get(pos));

			// If the tile is a double letter value tile, multiply the point value of the letter by two
			if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.DoubleLetter) {
				pointSum += move.get(pos).getPointValue() * 2;
			}
			// If the tile is a triple letter value tile, multiply the point value of the letter by three
			else if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.TripleLetter) {
				pointSum += move.get(pos).getPointValue() * 3;
			}
			// Else, add the normal point value of the letter to the sum
			else {
				pointSum += move.get(pos).getPointValue();
			}

			// If the tile is a double word score, set the boolean value doubleWordScore to true
			if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.DoubleWord) {
				doubleWordScore = true;
			}
			// Else if the tile is a triple word score, set the boolean value tripleWordScore to true
			else if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.TripleWord) {
				tripleWordScore = true;
			}
		}

		// If doubleWordScore and/or tripleWordScore is true, multiply the total points by two and/or three, respectively
		if (doubleWordScore == true) pointSum *= 2;
		if (tripleWordScore == true) pointSum *= 3;

		// Update the player's point total
		player.updatePlayerPoints(pointSum);
	}
	
	public boolean checkValidWord(Move move) {
		// This method checks the HashMap of Scrabble legal words to see if the played word is legal
		return wordMap.getWord(formWord(move).hashCode()) != null;
	}

	private String formWord(Move move) {
		/*
		* This helper method forms the full word from the GamePieces placed on the board, checking 
  		* to see if the placed GamePieces extend another word, before returning it to the checkValidWord method.
		*/

		// Gets the starting coordinates of the intended word to be played. 
		Position startWord = getStartOfWord(move.getStartPosition(), move.getDirection());
		int x = startWord.getX();
		int y = startWord.getY();
		StringBuilder word = new StringBuilder();

		// Iterates through the positions to see if the placement of the word is valid
		while (x >= 0 && x < board[0].length && y >= 0 && y < board.length) {
			if (move.getAtPosition(x, y) != null) {
				word.append(move.getAtPosition(x, y).getLetter());
			} else if (board[y][x].getPiece() != null) {
				word.append(board[y][x].getPiece().getLetter());
			} else {
				break;
			}

			// Updates the axis corresponding to the move's directionality
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
		* This helper method uses the start of the move to find the start of the word,
  		* which can be different if the player's move extends another word. 
		*/
		int x = startMove.getX();
		int y = startMove.getY();

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


	public String searchAroundLetter(Move move) {
		// This method sets the directionality of the move, based on the
		// results of the helper method getDirectionAroundSingleLetter
		if (getDirectionAroundSingleLetter(move) != null) {
			move.setDirection(getDirectionAroundSingleLetter(move));
			return formWord(move);
		}
		return null;
	}
	
	private Move.Directions getDirectionAroundSingleLetter(Move move) {
		// This helper method searches the tiles around the move to determine the direcitonality
		// of the move, and is most often used when the move consists of a single letter. 
		for (Position pos : move.getMove().keySet()) {
			if (pos.getY() != 0 && pos.getX() != 0) {
				// Checks to see if Tiles are occupied by GamePieces in a vertical direction
				if (board[pos.getY() - 1][pos.getX()].getPiece() != null || board[pos.getY() + 1][pos.getX()].getPiece() != null) {
					return Move.Directions.Vertical;
				}
				// Checks to see if Tiles are occupied by GamePieces in a horizontal direction. 
				if (board[pos.getY()][pos.getX() - 1].getPiece() != null || board[pos.getY()][pos.getX() + 1].getPiece() != null) {
					return Move.Directions.Horizontal;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		// This method returns a String representation of the GameBoard
		String result = ""; // Initialize the result string

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
