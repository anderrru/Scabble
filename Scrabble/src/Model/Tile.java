package Model;

public class Tile {

	private GamePiece piece;
	
	public Tile() {
		piece = null;
	}
	
	public boolean isEmpty() {
		return this.piece == null;
	}
	
	public void setPiece(GamePiece piece) {
		this.piece = piece;
	}
	
}
