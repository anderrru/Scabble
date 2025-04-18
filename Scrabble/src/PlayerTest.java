package tests;

import Model.Player;
import Model.Hand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testPlayerName() {
        Player player = new Player("Andrew");
        assertEquals("Andrew", player.getName());
    }

    @Test
    public void testPlayerHandIsNotNull() {
        Player player = new Player("Joe");
        assertNotNull(player.getHand());
    }

    @Test
    public void testHandHasTiles() {
        Player player = new Player("Steve");
        Hand hand = player.getHand();
        assertTrue(hand.size() > 0 && hand.size() <= 7, "Hand should have between 1 and 7 tiles");
    }

    @Test
    public void testPlayerHandToStringFormat() {
        Player player = new Player("Bob");
        String handString = player.getPlayerHand();

        assertTrue(handString.startsWith("Hand:"), "Should start with 'Hand:'");
    }
}
