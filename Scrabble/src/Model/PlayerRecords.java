/*
 * Authors: Erik Picazzo, Johnathan Alexander, Andrew Wong, Andrew Huynh
 * 
 * Description: This class manages any given player's Win/Loss Record
 * 		keeping track of them by writing and reading from a text file named
 * 		"playerSaves.txt" which has a list of players in the format:
 * 		playerName : wins,losses
 */

package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class PlayerRecords {
	private HashMap<String, int[]> players;

	// Constructor
	public PlayerRecords(){
		players = new HashMap<String, int[]>();
		readPlayerSaves();
	}
	
	private void readPlayerSaves() {
		/*
		 * This method reads the PlayerSaves.txt file that stores player data and 
		 * stores it into a HashMap
		 */
		File saves = new File("PlayerSaves.txt");
		try {
			Scanner savesScanner = new Scanner(saves);
			while(savesScanner.hasNext()) {
				String[] playerInfo = savesScanner.nextLine().split(" : ");
				int wins = Integer.valueOf(playerInfo[1].split(",")[0]);
				int losses = Integer.valueOf(playerInfo[1].split(",")[1]);
				players.put(playerInfo[0], new int[]{wins, losses});
			} 
			savesScanner.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void writePlayerSaves() {
		/*
		 * This method writes the PlayerSaves.txt file with updated values
		 * from the players HashMap;
		 */
		File saves = new File("PlayerSaves.txt");
		try {
			FileWriter writer = new FileWriter(saves);
			for (String p: players.keySet()) {
				writer.write(p + " : " + players.get(p)[0] + "," + players.get(p)[1] + "\n");	
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addPlayer(String playerName) {
		/*
		 * This method adds new players to the database with an initial 
   		 * win/loss score of 0/0
		 */
		if (!players.containsKey(playerName)) {
			players.put(playerName, new int[] {0,0});
		}
	}
	
	public int getWins(String playerName) {
		/*
		 * This method returns a specific player's number of wins
		 */
		return players.get(playerName)[0];
	}
	
	public int getLosses(String playerName) {
		/*
		 * This method returns a specific player's number of losses
		 */
		return players.get(playerName)[1];
	}
	
	public Set<String> getPlayerNames() {
		// This method returns a set of all player names within the database
		return players.keySet();
	}
	
	public void incrementWins(String playerName) {
		// This method increases the number of wins for a specific player
		if (players.containsKey(playerName)) {
			players.get(playerName)[0] += 1;
		}
	}
	
	public void incrementLosses(String playerName) {
		// This method increases the number of losses for a specific player
		if (players.containsKey(playerName)) {
			players.get(playerName)[1] += 1;
		}
	}
  
}
