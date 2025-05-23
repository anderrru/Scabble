/*
 * Authors: Johnathan Alexander, Erik Picazzo, Andrew Wong, Andrew Huynh
 * 
 * Description: This file is used primarily for sorting players by their wins
 * 		and losses in the playerRecordsClass
 */
package Model;

import java.util.Comparator;

public class PlayerStats{
	private Integer wins, losses;
	private String name;

	// Constructor
	public PlayerStats(String name, int wins, int losses) {
		this.name = name;
		this.wins = wins;
		this.losses = losses;
	}
	
	public Integer getWins() {
		// This method returns the number of times a player has won the game
		return wins;
	}

	public Integer getLosses() {
		// This method returns the number of time a player has lost the game
		return losses;
	}

	public String getName() {
		// This method returns the player's name
		return name;
	}

	public static Comparator<PlayerStats> sortByWinsComparator() {
		/* 
  		* This method utilizes the Comparator interface, and its required
		* method 'compare' in order to compare two PlayerStat objects by
		* their wins. 
  		*
    		* If       p1.wins < p2.wins, comp = -1
      		* else if  p1.wins == p2.wins, comp = 0
		* else	   p2.losses > p1.losses, comp = 1
		*/
		return new Comparator<PlayerStats>() {
			public int compare(PlayerStats p1, PlayerStats p2) {
				int comp = p2.wins.compareTo(p1.wins);
				if(comp == 0)
					comp = p1.losses.compareTo(p2.losses);
				return comp;
			}
		};
	}
}