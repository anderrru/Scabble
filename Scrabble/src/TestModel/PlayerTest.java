package TestModel;

import Model.GamePiece;
import Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("Player");
        GamePiece.clearUsedPieces();
    }

    @Test
    public void testGetName() {
        assertEquals("Player", player.getName());
    }

    @Test
    public void testInitialHandSize() {
        assertTrue(player.getPlayerPieces().size() <= 7 && player.getPlayerPieces().size() > 0);
    }

    @Test
    public void testGetPlayerHandFormat() {
        String hand = player.getPlayerHand();
        assertTrue(hand.startsWith("| "));
        assertTrue(hand.contains("|"));
    }

    @Test
    public void testAddToHandIncreasesSize() {
        player.getPlayerPieces().clear(); 
        
        int before = player.getPlayerPieces().size(); 
        GamePiece newPiece = GamePiece.getPiece();
        player.addToHand(newPiece);
        assertEquals(before + 1, player.getPlayerPieces().size()); 
    }

    @Test
    public void testGetPieceRemovesFromHand() {
        GamePiece original = player.getPlayerPieces().get(0);
        String letter = original.getLetter();
        int before = player.getPlayerPieces().size();

        GamePiece removed = player.getPiece(letter);
        assertNotNull(removed);
        assertEquals(letter, removed.getLetter());
        assertEquals(before - 1, player.getPlayerPieces().size());
    }

    @Test
    public void testUpdatePlayerPoints() {
        assertEquals(0, player.getPlayerPoints());
        player.updatePlayerPoints(10);
        assertEquals(10, player.getPlayerPoints());
    }

    @Test
    public void testIteratorOverHand() {
        int count = 0;
        for (GamePiece g : player) {
            assertNotNull(g);
            count++;
        }
        assertEquals(player.getPlayerPieces().size(), count);
    }
    
    @Test
    public void testSwapTiles() {
    	if (player.getPlayerPieces().size() >= 2) {
    		ArrayList<GamePiece> toSwap = new ArrayList<>();    	
    		toSwap.add(player.getPlayerPieces().get(0));
    		toSwap.add(player.getPlayerPieces().get(1));
    		player.swap(toSwap);
    		
    		assertEquals(7, player.getPlayerPieces().size());
    	}
    }
    
    @Test
    public void testFillPlayerHand() {
    	player.getPlayerPieces().clear();
    	assertEquals(0, player.getPlayerPieces().size());
    	
    	player.fillPlayerHand();
    	assertEquals(7, player.getPlayerPieces().size());
    }
}
