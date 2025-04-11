package Model;

import java.util.HashMap;

public class GamePiece {
	
	private final Letter letter;
	private final PointValue value;
	
	private GamePiece(Letter letter, PointValue val) {
		this.letter = letter;
		this.value = val;
	}
	
	// Need static getter method
	public static HashMap<String, GamePiece> getPiece() {
		return map;
	}
	
	private static HashMap<String, GamePiece> map = new HashMap<String, GamePiece>();
	
	static {
		for (Letter l : Letter.values()) {
			
			if (l.equals(Letter.A) || l.equals(Letter.I)) {
				for (int i = 1; i < 10; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}
			
			else if (l.equals(Letter.E)) {
				for (int i = 1; i < 13; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}
			
			else if (l.equals(Letter.O)) {
				for (int i = 1; i < 9; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}
			
			else if (l.equals(Letter.N) || l.equals(Letter.R)|| l.equals(Letter.T)) {
				for (int i = 1; i < 7; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}
			
			else if (l.equals(Letter.U) || l.equals(Letter.S) || l.equals(Letter.L)) {
				for (int i = 1; i < 5; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.One);
					map.put(key, t);
				}
			}
			
			else if (l.equals(Letter.D)) {
				for (int i = 1; i < 5; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.Two);
					map.put(key, t);
				}
			}
			
			else if (l.equals(Letter.G)) {
				for (int i = 1; i < 4; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.Two);
					map.put(key, t);
				}
			}
			
			else if (l.equals(Letter.B)|| l.equals(Letter.C) || l.equals(Letter.M) || l.equals(Letter.P)) {
				for (int i = 1; i < 3; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.Three);
					map.put(key, t);
				}
			}
			
			else if (l.equals(Letter.F) || l.equals(Letter.H) || l.equals(Letter.V) || l.equals(Letter.W) || l.equals(Letter.Y)) {
				for (int i = 1; i < 3; i++) {
					String key = "" + l + i;
					GamePiece t = new GamePiece(l, PointValue.Four);
					map.put(key, t);
				}
			}
			
			else if (l.equals(Letter.K)) {
				String key = "" + l + 1;
				GamePiece t = new GamePiece(l, PointValue.Five);
				map.put(key, t);
			}
			
			else if (l.equals(Letter.J) || l.equals(Letter.X)) {
				String key = "" + l + 1;
				GamePiece t = new GamePiece(l, PointValue.Eight);
				map.put(key, t);
			}
			
			else if (l.equals(Letter.Q) || l.equals(Letter.Z)) {
				String key = "" + l + 1;
				GamePiece t = new GamePiece(l, PointValue.Ten);
				map.put(key, t);
			}
			
		}
	}
	
	@Override
	public String toString() {
		return "" + this.letter + ": with a point value of: " + this.value;
	}
	
	public String getLetter() {
		return "" + this.letter;
	}

}
