package TestModel;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import Model.PlayerRecords;
import Model.PlayerStats;

class PlayerRecordsTest {
	private static HashMap<String, int[]> ogPlayerData;
	public static PlayerRecords records;
	
	@BeforeAll
	static void setUp(){
		ogPlayerData = new HashMap<String, int[]>();
		File oldRecords = new File("PlayerSaves.txt");
		Scanner preserveScanner;
		try {
			// Save any data that might already be in PlayerSaves.txt
			preserveScanner = new Scanner(oldRecords);
			while(preserveScanner.hasNext()) {
				String[] playerInfo = preserveScanner.nextLine().split(" : ");
				int wins = Integer.valueOf(playerInfo[1].split(",")[0]);
				int losses = Integer.valueOf(playerInfo[1].split(",")[1]);
				ogPlayerData.put(playerInfo[0], new int[]{wins, losses});
			} 
			preserveScanner.close();
			
			// Clear the file
			FileWriter writer = new FileWriter(oldRecords);
			writer.close();
			
			records = new PlayerRecords();
			records.addPlayer("Test1");
			records.addPlayer("Test2");
			records.incrementWins("Test1");
			records.incrementWins("Test1");
			records.incrementWins("Test2");
			records.incrementLosses("Test1");
			records.incrementLosses("Test2");
			records.incrementLosses("Test2");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	static void tearDown() throws Exception {
		// Restore the original data in PlayerSaves.txt
		File oldRecords = new File("PlayerSaves.txt");
		try {
			FileWriter writer = new FileWriter(oldRecords);
			for (String p: ogPlayerData.keySet()) {
				writer.write(p + " : " + ogPlayerData.get(p)[0] + "," + ogPlayerData.get(p)[1] + "\n");	
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testAddPlayer() {
		records.addPlayer("Test1");
		records.addPlayer("mike");
		
		assertTrue(records.getPlayerNames().size() == 3);
		assertTrue(records.getPlayerNames().contains("Test1"));
		assertTrue(records.getPlayerNames().contains("Test2"));
		assertTrue(records.getPlayerNames().contains("mike"));
	}
	
	@Test
	void testIncrementWins() {
		records.incrementWins("Test1");
		records.incrementWins("Fake");
		
		assertEquals(3, records.getWins("Test1"));
		assertEquals(1, records.getWins("Test2"));
	}

	@Test
	void testIncrementLosses() {
		records.incrementLosses("Test1");
		records.incrementLosses("Fake");
		
		assertEquals(2, records.getLosses("Test1"));
		assertEquals(2, records.getLosses("Test2"));
	}
	
	@Test
	void testGetWins() {
		assertEquals(2, records.getWins("Test1"));
		assertEquals(1, records.getWins("Test2"));
	}
	
	@Test
	void testGetLosses() {
		assertEquals(2, records.getLosses("Test1"));
		assertEquals(2, records.getLosses("Test2"));
	}
	
	@Test
	void testRestoreData() {
		records.writePlayerSaves();
		PlayerRecords instance2 = new PlayerRecords();
		
		assertEquals(2, instance2.getWins("Test1"));
		assertEquals(1, instance2.getWins("Test2"));
	}
	
	@Test
	void testGetSorted() {
		ArrayList<PlayerStats> stats = records.getSorted();
		assertEquals("Test1", stats.get(0).getName());
		assertEquals("Test2", stats.get(1).getName());
	}
}
