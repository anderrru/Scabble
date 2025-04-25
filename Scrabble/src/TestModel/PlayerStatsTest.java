package TestModel;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.PlayerStats;

class PlayerStatsTest {
	private static PlayerStats p1, p2;
	@BeforeEach
	void setUp() throws Exception {
		p1 = new PlayerStats("Test1", 2, 3);
		p2 = new PlayerStats("Test2", 3, 2);
	}


	@Test
	void testGetWins() {
		assertEquals(2, p1.getWins());
	}

	@Test
	void testGetLosses() {
		assertEquals(3, p1.getLosses());
	}
	
	@Test
	void testGetName() {
		assertEquals("Test1", p1.getName());
	}
	
	@Test
	void testSortWins() {
		Comparator<PlayerStats> comp = PlayerStats.sortByWinsComparator();
        assertTrue(comp.compare(p1, p2) > 0);
	}
	
	@Test
	void testSortWinsLessThan() {
		PlayerStats p3 = new PlayerStats("Test3", 3,0);
		Comparator<PlayerStats> comp = PlayerStats.sortByWinsComparator();
        assertTrue(comp.compare(p2, p3) > 0);
	}
	
}
