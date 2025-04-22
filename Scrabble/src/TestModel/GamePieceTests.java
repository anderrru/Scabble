package TestModel;

import Model.GamePiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamePieceTests {

    @Test
    public void testRemainingTilesInitiallyCorrect() {
        int remaining = GamePiece.remainingTiles();
        assertTrue(remaining > 0);
        // Given we will remove 1 GamePiece then new pool will 
        // contain 97 pieces instead of 98
        assertEquals(97, remaining); 
    }

    @Test
    public void testGetPieceReducesBagSize() {
        int before = GamePiece.remainingTiles();
        GamePiece piece = GamePiece.getPiece();
        int after = GamePiece.remainingTiles();

        assertNotNull(piece);
        assertEquals(before - 1, after);
    }

    @Test
    public void testGetLetterIsUppercase() {
        GamePiece piece = GamePiece.getPiece();
        assertNotNull(piece);

        String letter = piece.getLetter();
        assertTrue(letter.matches("[A-Z]"));
    }


   @Test
    public void testGetPieceReturnsNullIfEmpty() {
        while (GamePiece.getPiece() != null) { }

        assertEquals(0, GamePiece.remainingTiles());
        assertNull(GamePiece.getPiece());
    }
}
