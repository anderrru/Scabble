package Model;

public class Tile {

	private GamePiece piece;
	public enum Type {Blank, Center, TripleWord, TripleLetter, DoubleWord, DoubleLetter};
	private Type type;
	private boolean used;
	
	
	public Tile() {
		piece = null;
	}
	
	public boolean isEmpty() {
		return this.piece == null;
	}
	
	public void setPiece(GamePiece piece) {
		this.piece = piece;
	}
	
	public void setSpecial(Type type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		if (this.piece != null) {
			return "A"; // Has to be the piece's letter but I don't currently know how to get that
		}
		else {
			if (type == Type.Center) return "**";
			else if (type == Type.TripleWord) return "TW";
			else if (type == Type.TripleLetter) return "TL";
			else if (type == Type.DoubleWord) return "DW";
			else if (type == Type.DoubleLetter) return "DL";
			else return "  ";
		}
	}
	
}
