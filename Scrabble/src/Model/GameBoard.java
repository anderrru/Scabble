package Model;

import java.util.Arrays;
import java.util.HashMap;

import Model.Tile.Type;

public class GameBoard {
	private Tile[][] board;
	private WordMap wordMap;

	public GameBoard() {
		board = new Tile[15][15];
		for (int i = 0; i<board.length; i++) {
			for (int j = 0; j<board.length; j++) {
				board[i][j] = new Tile();
			}
		}
		setSpecialTiles();
		wordMap = new WordMap();

	}

	private void setSpecialTiles() {
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
			return board[y][x];
		}

		public void playerMove(Move move, boolean firstMove, Player player) {
			if (firstMove && checkValidMoves(move.getMove()) || checkValidMoves(move.getMove()) /*&& checkValidWord(move)*/) {
				System.out.println(formWord(move));
				place(move.getMove(), player);
			}
		}

		// helper method for checking valid move
		private boolean checkValidMoves(HashMap<Position, GamePiece> move) {
			for (Position pos : move.keySet()) {
				// Passing a word using a letter that already exists within the board (the existing tile bit) causes this to return false,
				// since the piece isn't in move.
				if (!board[pos.getY()][pos.getX()].isEmpty() && !board[pos.getY()][pos.getX()].getPiece().getLetter().equals(move.get(pos).getLetter())
					|| (board[pos.getY()][pos.getX()].getPiece() != move.get(pos)) && (board[pos.getY()][pos.getX()].getPiece() != null)) {
					return false;
				}
			}
			return true;
		}

		// helper method to place the gamepieces from move onto board
		private void place(HashMap<Position, GamePiece> move, Player player) {
			int pointSum = 0;
			boolean doubleWordScore = false;
			boolean tripleWordScore = false;

			for (Position pos : move.keySet()) {
				board[pos.getY()][pos.getX()].setPiece(move.get(pos));
				
				if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.DoubleLetter) {
					pointSum += move.get(pos).getPointValue() * 2;
				}
				else if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.TripleLetter) {
					pointSum += move.get(pos).getPointValue() * 3;
				}
				else {
					pointSum += move.get(pos).getPointValue();
				}
				
				if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.DoubleWord) {
					doubleWordScore = true;
				}
				else if (board[pos.getY()][pos.getX()].getSpecial() == Tile.Type.TripleWord) {
					tripleWordScore = true;
				}
			}
			
			if (doubleWordScore == true) pointSum *= 2;
			if (tripleWordScore == true) pointSum *= 3;
			
			player.updatePlayerPoints(pointSum);
		}
		
		private boolean checkValidWord(Move move) {
			Position startMove = move.getStartPosition();
			Position startWord = getStartOfWord(startMove, move.getDirection());
			int x = startWord.getX();
			int y = startWord.getY();
			System.out.println("X is " + x);
			System.out.println(y);
			String word = "Nifty";
			System.out.println("Board at y/x is " + board[y][x].getPiece());
			while (board[y][x].getPiece() != null) {
				word += board[y][x].getPiece().getLetter();
				System.out.println("Current letter is " + board[y][x].getPiece().getLetter());
				if (move.getDirection() == Move.Directions.Horizontal) {
					x++;
				}
				else if (move.getDirection() == Move.Directions.Vertical) {
					y++;
				}
			}
			System.out.println("The word is " + word);
			return wordMap.getWord(word.toString().hashCode()) != null;
		}

	private String formWord(Move move) {
		Position startWord = getStartOfWord(move.getStartPosition(), move.getDirection());
		int x = startWord.getX();
		int y = startWord.getY();
		StringBuilder word = new StringBuilder();

		while (x >= 0 && x < board[0].length && y >= 0 && y < board.length) {
			if (move.getAtPosition(x, y) != null) {
				word.append(move.getAtPosition(x, y).getLetter());
			} else if (board[y][x].getPiece() != null) {
				word.append(board[y][x].getPiece().getLetter());
			} else {
				break;
			}

			if (move.getDirection() == Move.Directions.Horizontal) {
				x++;
			} else {
				y++;
			}
		}

		return word.toString();
	}


	private Position getStartOfWord(Position startMove, Move.Directions direction) {
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

		System.out.println("Start of word at: x=" + x + ", y=" + y);
		return new Position(x, y);
	}


		@Override
		public String toString() {
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
