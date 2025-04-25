/*
* Authors: Johnathan Alexander, Erik Picazzo, Andrew Wong, Andrew Huynh
*
* Description: This file represents an x/y coordinate, and further contains
*		sorting methods for comparing two different positions by
*		either their x-coordinate or their y-coordinate. 
*/

package Model;

import java.util.Comparator;

public class Position{
	private Integer x;
	private Integer y;

	// Constructor
	public Position (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		// This method returns the x-coordinate
		return x;
	}
	public int getY() {
		// This method returns the y-coordinate
		return y;
	}

	public static Comparator<Position> sortByXComparator() {
		/* 
  		* This method utilizes the Comparator interface, and its required
		* method 'compare' in order to compare two Position objects by
		* their x-coordinates. 
  		*
    		* If       p1.x < p2.x, comp = -1
      		* else if  p1.x == p2.x, comp = 0
		* else	   p1.x > p2.x, comp = 1
		*/
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
		/*
		* This method utilizes the Comparator interface, and its required
  		* method 'compare' in order to compare two Position objects by
    		* their y-coordinates.
      		*
		* If       p1.y < p2.y, comp = -1
  		* else if  p1.y == p2.y, comp = 0
    		* else     p1.y > p2.y, comp = 1
		*/
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
