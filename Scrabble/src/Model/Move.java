/*
* Authors: Johnathan Alexander, Erik Picazzo, Andrew Wong, Andrew Huynh
*
* Description: This file contains compiled information for a player's selected move,
*		including the direction they want to play in, the starting coordinates
*		of their play, and the subsequent coordinates of the remaining letters
*		they play all within a HashMap to be used in later checks. 
*/

package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Move {
	private HashMap<Position, GamePiece> move;
	public enum Directions {Vertical, Horizontal, Both};
	private Directions direction;
	private int startX;
	private int startY;
	private WordMap wordMap;

	// Constructor
	public Move() {
		this.move = new HashMap<>();
		this.wordMap = new WordMap();
	}

	public void addPiece(GamePiece piece, int x, int y) {
		/*
		 * This method adds a game piece to the move HashMap at a given x/y position,
   		 * in the form of a Position object. 
		 */
		Position pos = new Position(x, y);
		move.put(pos, piece);
	}

	public void clear() {
		// This method clears the move HashMap
		move.clear();
	}

	public int getX() {
		// This method returns the beginning x-coordinate of a Move
		return this.startX;
	}

	public int getY() {
		// This method returns the beginning y-coordinate of a Move
		return this.startY;
	}

	public void setPlacement(int x, int y, Directions direction) {
		// This method initializes the beginning x, y, and direction of a Move object
		this.startX = x;
		this.startY = y;
		this.direction = direction;
	}

	// Do we really need this? Move shouldn't be something that can be changed after the initial stuff, yeah?
	public void setDirection(Directions direction) {
		// This method sets the direction of a Move object
		this.direction = direction;
	}
	
	public HashMap<Position, GamePiece> getMove() {
		// This method returns the move HashMap
		return this.move;
	}

	public GamePiece getPiece(Position pos) {
		// This method returns the GamePiece associated with a given x/y Position object
		return this.move.get(pos);
	}

	public ArrayList<Position> getPositionsbyX(){
		// This method utilizes Collections, in conjunction with the Comparator interface, 
		// to sort all of the Position objects in the move HashMap by their x-coordinate value,
		// in increasing numerical order
		ArrayList<Position> positions = new ArrayList<Position>(move.keySet());
		Collections.sort(positions, Position.sortByXComparator());
		return positions;
	}
	
	public ArrayList<Position> getPositionsbyY(){
		// This method utilizes Collections, in conjunction with the Comparator interface,
		// to sort all of the Position objects in the move HashMap by their y-coordinate value,
		// in increasing numerical order. 
		ArrayList<Position> positions = new ArrayList<Position>(move.keySet());
		Collections.sort(positions, Position.sortByYComparator());
		return positions;
	}

	public Directions getDirection() {
		/*
		* This method returns the direction of the move.
  		*
    		* In the event that the direction variable has not yet been set,
      		* the method calculates the direction by ordering the x and y coordinates
		* via the Comparator interface, then checks to see which axis value
  		* undergoes changes. If the y-coordinate changes as the Positions are
    		* sorted, then the direction of the Move is vertical. Else, it is horizontal.
		*/
		if (this.direction != null) {
			return this.direction;
		}
		ArrayList<Position> sortByX = this.getPositionsbyX();
		ArrayList<Position> sortByY = this.getPositionsbyY();
		Directions direction = null;
		int i = 1;

		// Checks to see which axis is changing as the Position objects are iterated. 
		while (i < move.size()) {
			if (sortByY.get(i).getX() == sortByY.get(i-1).getX()) direction = Directions.Vertical;
			else direction = Directions.Horizontal;
			i++;
		}
		
		return direction;
	}

	public Position getStartPosition() {
		// This method returns the start coordinate of the move HashMap
		Move.Directions direction = this.getDirection();

		// First checks to see if a direction has been set, or if the Move HashMap 
		// has attempted placements of GamePieces
		if (direction == null || move.isEmpty()) return null;

		// If a direction has been set, it checks the directionality to ensure it returns the proper value
		// by ordering the Position objects in the HashMap by their x-coordinate for Horizontal, and 
		// y-coordinate for Vertical. 
		if (direction == Directions.Horizontal) {
			return this.getPositionsbyY().get(0);
		} else {
			return this.getPositionsbyX().get(0);
		}
	}


	public GamePiece getAtPosition(int x, int y) {
		// This method returns the GamePiece at its associated x/y Position object. 
		for (Position p : move.keySet()) {
			if (p.getY() == y && p.getX() == x) {
				return move.get(p);
			}
		}
		return null;
	}

}
