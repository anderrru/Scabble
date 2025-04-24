
/*
* Authors: Johnathan Alexander, Erik Picazzo, Andrew Wong, Andrew Huynh
* 
* Description: This file initializes and contains the 98 total game pieces used in the game
* 		of modified Scrabble (the two missing pieces are the blank tiles,
*		which are not a required part of this project's functionality).
*		It implements the Flyweight design pattern to ensure that all
*		game piece objects are unique. Each GamePiece has an associated
*		enum Letter and PointValue, and the multiplicity of each tile
*		corresponds to the official Scrabble piece counts. 
*/

package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GamePiece {
	
	private final Letter letter;
	private final PointValue value;

	// Constructor
	private GamePiece(Letter letter, PointValue val) {
		this.letter = letter;
		this.value = val;
	}

	// Static store to store all unique instances of GamePiece
	private static HashMap<String, GamePiece> map = new HashMap<String, GamePiece>();

	// Static block used to implement Flyweight design pattern. 
	static {
		// Iterates through the entire alphabet
		for (Letter l : Letter.values()) {

			// Creates 10 unique game pieces for both letters A and I, with a point value of one
			if (l.equals(Letter.A) || l.equals(Letter.I)) {
				for (int i = 1; i < 10; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}

			// Creates 13 unique game pieces for the letter E, with a point value of one
			else if (l.equals(Letter.E)) {
				for (int i = 1; i < 13; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}

			// Creates 9 unique game pieces for the letter O, with a point value of one
			else if (l.equals(Letter.O)) {
				for (int i = 1; i < 9; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}

			// Creates 7 unique game pieces for each of the letters N, R, and T, each with a point value of one
			else if (l.equals(Letter.N) || l.equals(Letter.R)|| l.equals(Letter.T)) {
				for (int i = 1; i < 7; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}

			// Creates 5 unique game pieces for each of the letters U, S, and L, each with a point value of one
			else if (l.equals(Letter.U) || l.equals(Letter.S) || l.equals(Letter.L)) {
				for (int i = 1; i < 5; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}

			// Creates 5 unique game pieces for the letter D, with a point value of two
			else if (l.equals(Letter.D)) {
				for (int i = 1; i < 5; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.Two);
					map.put(key, t);
				}
			}

			// Creates 4 unique game pieces for the letter G, with a point value of two
			else if (l.equals(Letter.G)) {
				for (int i = 1; i < 4; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.Two);
					map.put(key, t);
				}
			}

			// Creates 3 unique game pieces for each of the letters B, C, M, and P, each with a point value of three
			else if (l.equals(Letter.B)|| l.equals(Letter.C) || l.equals(Letter.M) || l.equals(Letter.P)) {
				for (int i = 1; i < 3; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.Three);
					map.put(key, t);
				}
			}

			// Creates three unique game pieces for each of the letters B, C, M, and P, each with a point value of four
			else if (l.equals(Letter.F) || l.equals(Letter.H) || l.equals(Letter.V) || l.equals(Letter.W) || l.equals(Letter.Y)) {
				for (int i = 1; i < 3; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.Four);
					map.put(key, t);
				}
			}

			// Creates one unique game piece for the letter K, with a point value of five
			else if (l.equals(Letter.K)) {
				String key = "" + l + 1;
				GamePiece t = new GamePiece(l, PointValue.Five);
				map.put(key, t);
			}

			// Creates one unique game piece for each of the letters J and X, each with a point value of eight
			else if (l.equals(Letter.J) || l.equals(Letter.X)) {
				String key = "" + l + 1;
				GamePiece t = new GamePiece(l, PointValue.Eight);
				map.put(key, t);
			}

			// Creates one unique game piece for each of the letters Q and Z, each with a point value of ten
			else if (l.equals(Letter.Q) || l.equals(Letter.Z)) {
				String key = "" + l + 1;
				GamePiece t = new GamePiece(l, PointValue.Ten);
				map.put(key, t);
			}
			
		}
	}
	
	
	@Override
	public String toString() {
		// This method returns a String of a GamePiece, with it's letter and corresponding point value
		return "" + this.letter + ": with a point value of: " + this.value;
	}
	
	public String getLetter() {
		// This method returns the letter of a GamePiece
		return "" + this.letter;
	}
	
	public int getPointValue() {
		// This method returns and int value based on the corresponding enum value of a GamePiece
		if (this.value == PointValue.One) return 1;
		else if (this.value == PointValue.Two) return 2;
		else if (this.value == PointValue.Three) return 3;
		else if (this.value == PointValue.Four) return 4;
		else if (this.value == PointValue.Five) return 5;
		else if (this.value == PointValue.Eight) return 8;
		else if (this.value == PointValue.Ten) return 10;
		return 0;
	}
	
	public static GamePiece getPiece() {
		// This method returns a random GamePiece from the static store, simualting
		// picking a random piece in Scrabble. 
		if (map.isEmpty()) return null;
		ArrayList<String> keys = new ArrayList<>(map.keySet());
		Collections.shuffle(keys);
		
		GamePiece retPiece = map.remove(keys.get(0));
		return retPiece;
	}
	
	public static int remainingTiles() {
		// This method returns the size of the static store
	    	return map.size();
	}

}
