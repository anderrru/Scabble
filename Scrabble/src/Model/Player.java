package Model;

import java.util.ArrayList;

public class Player {
	private String name;
	private ArrayList<GamePiece> playerHand;
	
	public Player(String name) {
		this.name = name;
		this.playerHand = new ArrayList<GamePiece>();
		fillPlayerHand();
	}
	
	public void fillPlayerHand() {
		for (int i = playerHand.size(); i < 7; i++) {
			playerHand.add(GamePiece.getPiece());
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getPlayerHand() {
		String output = "| ";
		for (GamePiece g : playerHand) {
			output += g.getLetter() + " | ";
		}
		
		return output.trim();
	}
	
	public GamePiece getLetter(String letter) {
		for (GamePiece g : playerHand) {
			if (g.getLetter().equals(letter.toUpperCase())) {
				return g;
			}
		}
		
		return null;
	}
	
	public void removeFromHand(GamePiece g) {
		playerHand.remove(g);
	}
}
