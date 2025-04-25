package TestModel;

import Model.GamePiece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class GamePieceTests {
	
	@BeforeEach
	public void setUp() {
		GamePiece.clearUsedPieces();
	}

    @Test
    public void testTilesAmountInitiallyCorrect() {
        int remaining = GamePiece.getMapSize();
        assertTrue(remaining > 0);
        assertEquals(98, remaining); 
    }

    @Test
    public void testGetLetterIsUppercase() {
        GamePiece piece = GamePiece.getPiece();
        assertNotNull(piece);

        String letter = piece.getLetter();
        assertTrue(letter.matches("[A-Z]"));
    }


   @Test
    public void testGetPieceReturnsNullIfUsedFull() {
        while (GamePiece.getPiece() != null) { }

        assertEquals(0, GamePiece.remainingTiles());
        assertNull(GamePiece.getPiece());
    }
   
   @Test
   public void testUsedPiecesMapUpdates() {
	   assertEquals(98, GamePiece.remainingTiles());
	   
	   while (GamePiece.getPiece() != null) {
		   // Use all GamePieces
	   }
	   
	   assertEquals(0, GamePiece.remainingTiles());
   }
}
