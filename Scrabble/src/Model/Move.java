package Model;

import java.util.ArrayList;
import java.util.Iterator;

public class Move implements Iterable<GamePiece>{
	private ArrayList<GamePiece> move;
	public enum Directions {Vertical, Horizonal};
	private Directions direction;
	private int startX;
	private int startY;
	private WordMap wordMap;
	
	public Move() {
		this.move = new ArrayList<GamePiece>();
		this.wordMap = new WordMap();
	}
	
	public void addPiece(GamePiece piece) {
		/*
		 * This method adds a game piece
		 */
		move.add(piece);
	}
	
	public void clear() {
		move.clear();
	}
	
	public boolean checkValidity() {
		String currentWord = "";
		for (GamePiece letter: this) {
			currentWord += letter;
		}
		return wordMap.getWord(currentWord.toUpperCase().hashCode()) != null;
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

	@Override
	public Iterator<GamePiece> iterator() {
		return move.iterator();
	}
}