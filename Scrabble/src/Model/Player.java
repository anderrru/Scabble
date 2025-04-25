/*
* Authors: Johnathan Alexander, Erik Picazzo, Andrew Wong, Andrew Huynh
*
* Description: This file simulates an individual player of the Scrabble game, containing
*		the player's name, overall score throughout the game, as well as their hand
*		of Scrabble tiles, which holds a maximum of seven pieces, as per the game
*		standard. 
*/

package Model;

import java.util.ArrayList;
import java.util.Iterator;

public class Player implements Iterable<GamePiece> {
	private String name;
	private Hand playerHand;
	private int playerPoints;

	// Constructor
	public Player(String name) {
		this.name = name;
		playerPoints = 0;
		this.playerHand = new Hand();
	}
	
	public void fillPlayerHand() {
		// This method fills the player hand until it contains seven GamePiece objects
		for (int i = playerHand.size(); i < 7; i++) {
			playerHand.add(GamePiece.getPiece());
		}
	}
	
	public String getName() {
		// This method returns the player's name
		return name;
	}
	
	public String getPlayerHand() {
		// This method returns a String representation of the GamePiece objects
		// in the player's hand
		String output = "| ";
		for (GamePiece g : playerHand.getTiles()) {
			output += g.getLetter() + " | ";
		}
		
		return output.trim();
	}

	public GamePiece getPiece(String s) {
		// This method returns the GamePiece from the player's hand that has the
		// same letter as the argument String. If no such GamePiece exists in the player's
		// hand, the method returns null. 
		for (GamePiece g : playerHand.getTiles()) {
			if (g.getLetter().equals(s)) {
				playerHand.remove(g);
				return g;
			}
		}
		
		return null;
	}
	
	public ArrayList<GamePiece> getPlayerPieces() {
		// This method returns the player's hand as an ArrayList of GamePiece objects
		return playerHand.getTiles();
	}
	
	public void addToHand(GamePiece g) {
		// This method adds a GamePiece to the player hand
		playerHand.add(g);
	}
	
	public void updatePlayerPoints(int points) {
		// This method updates the player's overall point total
		playerPoints += points;
	}
	
	public int getPlayerPoints() {
		// This method returns the player's point total
		return this.playerPoints;
	}

	@Override
	public Iterator<GamePiece> iterator() {
		// This method implements the Iterable interface to return an Iterator
		// object of the player's hand of GamePieces
		return playerHand.getTiles().iterator();
	}
}
