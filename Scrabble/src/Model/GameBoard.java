package Model;

public class GameBoard {
	private Tile[][] board;
	
	public GameBoard() {
		board = new Tile[15][15];
	}
	
	public Tile getTile(int i, int j) {
		return board[i][j];
	}
	
	public void placeGamePiece(int i, int j) {
		Tile tile = getTile(i,j);
		if (tile.isEmpty()) {
			// TODO implement helper functions that checks if placement i, j is valid
		}
	}

	public boolean checkValidMove() {
		// TODO check if move is valid
	}
}
