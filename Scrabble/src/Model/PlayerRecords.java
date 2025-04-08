/*
 * Authors: Erik Picazzo, Johnathan Alexander, Andrew Wong, Andrew Huynh
 * 
 * Description: This class manages any given player's Win/Loss Record
 * keeping track of them by writing and reading from a text file named
 * "playerSaves.txt" which has a list of players in the format:
 * playerName : wins,losses
 */

package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class PlayerRecords {
	private HashMap<String, int[]> players;
	
	public PlayerRecords(){
		players = new HashMap<String, int[]>();
		readPlayerSaves();
	}
	
	private void readPlayerSaves() {
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
		if (!players.containsKey(playerName)) {
			players.put(playerName, new int[] {0,0});
		}
	}
	
	public int getWins(String playerName) {
		return players.get(playerName)[0];
	}
	
	public int getLosses(String playerName) {
		return players.get(playerName)[1];
	}
	
	public void incrementWins(String playerName) {
		if (players.containsKey(playerName)) {
			players.get(playerName)[0] += 1;
		}
	}
	
	public void incrementLosses(String playerName) {
		if (players.containsKey(playerName)) {
			players.get(playerName)[1] += 1;
		}
	}
  
}
