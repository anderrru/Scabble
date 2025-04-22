package TestModel;

import Model.PlayerRecords;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class PlayerRecordsTest {
	
	private PlayerRecords records;
	
	@BeforeEach
	public void setUp()	{
		records = new PlayerRecords();
		records.addPlayer("Player");
	}
	
	@Test
	public void testNewPlayerStats() {
		assertEquals(0, records.getWins("Player"));
		assertEquals(0, records.getLosses("Player"));
	}
	
	@Test
	public void testIncrementWins() {
		records.incrementWins("Player");
		assertEquals(1, records.getWins("Player"));
		
		records.writePlayerSaves();
	}
	
	@Test
	public void testIncrementLosses() {
		records.incrementLosses("Player");
		assertEquals(1, records.getLosses("Player"));
	}
	
	@Test
	public void testMultipleWinsAndLosses() {
		records.incrementWins("Player");
		records.incrementWins("Player");
		records.incrementWins("Player");
		records.incrementWins("Player");
		records.incrementLosses("Player");
		records.incrementLosses("Player");
		records.incrementLosses("Player");
		
		assertEquals(4, records.getWins("Player"));
		assertEquals(3, records.getLosses("Player"));
	}
	
	@Test
	public void testNotOverwriteExistingPlayer() {
		records.incrementWins("Player");
		records.addPlayer("Player");
		assertEquals(1, records.getWins("Player"));
	}
	
	@Test
	public void testReadPlayerFromFile() {
	    PlayerRecords fileRecords = new PlayerRecords();
	    assertEquals(2, fileRecords.getWins("TestPlayer"));
	    assertEquals(3, fileRecords.getLosses("TestPlayer"));
	}

}
