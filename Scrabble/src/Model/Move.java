package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Move {
	private HashMap<Position, GamePiece> move;
	public enum Directions {Vertical, Horizonal};
	private Directions direction;
	private int startX;
	private int startY;
	private WordMap wordMap;

	public Move() {
		this.move = new HashMap<>();
		this.wordMap = new WordMap();
	}

	public void addPiece(GamePiece piece, int x, int y) {
		/*
		 * This method adds a game piece
		 */
		Position pos = new Position(x, y);
		move.put(pos, piece);
	}

	public void clear() {
		move.clear();
	}

	public int getX() {
		return this.startX;
	}

	public int getY() {
		return this.startY;
	}

	public void setPlacement(int x, int y, Directions direction) {
		this.startX = x;
		this.startY = y;
		this.direction = direction;
	}
	public HashMap<Position, GamePiece> getMove() {
		return this.move;
	}

	public GamePiece getPiece(Position pos) {
		return this.move.get(pos);
	}

	public ArrayList<Position> getPositionsbyX(){
		ArrayList<Position> positions = new ArrayList<Position>(move.keySet());
		Collections.sort(positions, Position.sortByXComparator());
		return positions;
	}
	
	public ArrayList<Position> getPositionsbyY(){
		ArrayList<Position> positions = new ArrayList<Position>(move.keySet());
		Collections.sort(positions, Position.sortByYComparator());
		return positions;
	}

}
