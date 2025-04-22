package Model;

import java.util.Comparator;

public class Position{
	private Integer x;
	private Integer y;
	public enum Directions {Horizontal, Vertical};
	private Directions direction;
	
	public Position (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public static Comparator<Position> sortByXComparator() {
		return new Comparator<Position>() {
			public int compare(Position p1, Position p2) {
				int comp = p1.x.compareTo(p2.x);
				if(comp == 0)
					comp = p1.y.compareTo(p2.y);
				return comp;
			}
		};
	}
	
	public static Comparator<Position> sortByYComparator() {
		return new Comparator<Position>() {
			public int compare(Position p1, Position p2) {
				int comp = p1.y.compareTo(p2.y);
				if(comp == 0)
					comp = p1.x.compareTo(p2.x);
				return comp;
			}
		};
	}
}

