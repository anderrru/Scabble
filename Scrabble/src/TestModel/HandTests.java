package tests;

import org.junit.jupiter.api.Test;

import Model.GamePiece;
import Model.Hand;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HandTests {

	@Test
	public void testInitialHandSize() {
		Hand hand = new Hand();
		assertTrue(hand.size() <= 7);
		assertFalse(hand.getTiles().isEmpty());
	}

	@Test
	public void testAddAndRemoveTile() {
		Hand hand = new Hand();

		GamePiece removedPiece = hand.getTiles().get(0);
		hand.remove(removedPiece);

		GamePiece newPiece = GamePiece.getPiece();
		boolean added = hand.add(newPiece);
		assertTrue(added);

		boolean removed = hand.remove(newPiece);
		assertTrue(removed);
	}

	@Test
	public void testSwapTiles() {
		Hand hand = new Hand();
		List<GamePiece> currentTiles = hand.getTiles();

		if (currentTiles.size() >= 3) {
			List<GamePiece> toSwap = currentTiles.subList(0, 3);
			boolean result = hand.swapTiles(List.copyOf(toSwap));
			assertTrue(result);
			assertEquals(7, hand.size());
		}
	}

	@Test
	public void testClear() {
		Hand hand = new Hand();
		hand.clear();
		assertEquals(0, hand.size());
		assertTrue(hand.getTiles().isEmpty());
	}
	
	@Test
	public void testHandToStringFormat() {
	    Hand hand = new Hand();
	    String output = hand.toString();
	    assertNotNull(output);
	    assertTrue(output.startsWith("Hand:"), "String should start with 'Hand:'");
	    assertTrue(output.length() > 5, "Hand string should include at least one tile after 'Hand:'");
	}

}
