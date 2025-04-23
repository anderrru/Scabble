package Model;

import java.util.ArrayList;

public class Player {
	private String name;
	private ArrayList<GamePiece> playerHand;
	private int playerPoints;
	
	public Player(String name) {
		this.name = name;
		playerPoints = 0;
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
	
	public GamePiece getPiece(String s) {
		for (GamePiece g : playerHand) {
			if (g.getLetter().equals(s)) {
				playerHand.remove(g);
				return g;
			}
		}
		
		return null;
	}
	
	public ArrayList<GamePiece> getPlayerPieces() {
		return playerHand;
	}
	
	public void addToHand(GamePiece g) {
		playerHand.add(g);
	}
	
	public void updatePlayerPoints(int points) {
		playerPoints += points;
	}
	
	public int getPlayerPoints() {
		return this.playerPoints;
	}
}
