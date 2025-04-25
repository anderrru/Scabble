/*
* Authors: Johnathan Alexander, Erik Picazzo, Andrew Wong, Andrew Huynh
*
* Description: This method represents a tile space on a Scrabble game board. 
*		A tile can either be a regular tile space, or one of the following
*		special tile spaces: triple word score, double word score, double 
*		letter score, triple letter score. The center tile of the board is
*		also marked. If a Tile is not one of the listed special types, it is
*		blank. 
*
*		Tile objects can hold GamePiece objects, and are used to keep track of
*		available and already-played spaced on the overall GameBoard. Furthermore,
*		every Tile object contains a boolean 'used', which determines whether 
*		special tiles have already been activated in the game, and prevents them
*		from being used again. 
*/

package Model;

public class Tile {

	private GamePiece piece;
	public enum Type {Blank, Center, TripleWord, TripleLetter, DoubleWord, DoubleLetter};
	private Type type;
	private boolean used;
	
	// Constructor
	public Tile() {
		piece = null;
	}
	
	public boolean isEmpty() {
		// This method returns a boolean value depending on if the Tile is occupied
		// by a GamePiece object
		return this.piece == null;
	}
	
	public void setPiece(GamePiece piece) {
		/*
		* This method fills a Tile object with a GamePiece object, and sets the
		* boolean variable 'used' equal to true so that special tiles cannot be
		* used more than once.
		*/
		this.piece = piece;
		
		if (type != Type.Blank && type != Type.Center) {
			this.used = true;
		}
	}

	public GamePiece getPiece() {
		// This method returns the GamePiece object associated with the Tile object.
		return this.piece;
	}

	public void setSpecial(Type type) {
		// This method sets a Tile object to the argument "special" type
		this.type = type;
	}

	public Type getSpecial() {
		// This method returns the special type of a Tile objet. 
		return this.type;
	}

	@Override
	public String toString() {
		/*
		*This method returns a String representation of a Tile obejct. If the Tile
  		* isn't occupied by a GamePiece, it will return an empty space if the Tile
    		* is not a special tile, and different, later replaced representations of the
      		* type of special tile; TW for triple word score, TL for triple letter score,
		* DW for double word score, and DL for double letter score. 
		*/ 
		if (this.piece != null) {
			return piece.getLetter() + " "; 
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
