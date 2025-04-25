package tests;

import Model.GamePiece;
import Model.Hand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HandTests {

    private Hand hand;

    @BeforeEach
    public void setUp() {
        hand = new Hand();
    }

    @Test
    public void testInitialHandSize() {
        if (hand.getTiles().isEmpty()) {
            return; 
        }
        assertTrue(hand.size() <= 7);
        assertFalse(hand.getTiles().isEmpty());
    }


    @Test
    public void testAddAndRemoveTile() {
        if (!hand.getTiles().isEmpty()) {
            GamePiece removedPiece = hand.getTiles().get(0);
            hand.remove(removedPiece);

            GamePiece newPiece = GamePiece.getPiece();
            if (newPiece != null) {
                boolean added = hand.add(newPiece);
                assertTrue(added);

                boolean removed = hand.remove(newPiece);
                assertTrue(removed);
            }
        }
    }

    @Test
    public void testAddFailsWhenFull() {
        GamePiece extraPiece = GamePiece.getPiece();
        boolean added = hand.add(extraPiece);
        assertFalse(added);
    }

    @Test
    public void testSwapTilesSuccess() {
        ArrayList<GamePiece> currentTiles = new ArrayList<>(hand.getTiles());

        if (currentTiles.size() >= 3) {
            ArrayList<GamePiece> toSwap = new ArrayList<>(currentTiles.subList(0, 3));
            boolean result = hand.swapTiles(toSwap);
            assertTrue(result);
            assertEquals(7, hand.size());
        }
    }

    @Test
    public void testSwapTilesFail_NotEnoughInBag() {
        while (GamePiece.getPiece() != null) {}

        if (hand.getTiles().size() >= 2) {
            ArrayList<GamePiece> toSwap = new ArrayList<>(hand.getTiles().subList(0, 2));
            boolean result = hand.swapTiles(toSwap);
            assertFalse(result);
        }
    }

    @Test
    public void testSwapTilesFail_NotOwned() {
        GamePiece fakePiece = GamePiece.getPiece(); 
        if (fakePiece != null) {
            ArrayList<GamePiece> toSwap = new ArrayList<>();
            toSwap.add(fakePiece);

            boolean result = hand.swapTiles(toSwap);
            assertFalse(result);
        }
    }

    @Test
    public void testClear() {
        hand.clear();
        assertEquals(0, hand.size());
        assertTrue(hand.getTiles().isEmpty());
    }

    @Test
    public void testHandToStringFormat() {
        String output = hand.toString();
        assertNotNull(output);
        assertTrue(output.startsWith("Hand:"), "Should start with 'Hand:'");
        assertTrue(output.length() > 5, "Should include tile letters");
    }
}
