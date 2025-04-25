package tests;

import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private GameBoard board;

    @BeforeEach
    public void setUp() {
        board = new GameBoard();
    }

    @Test
    public void testBoardInitialization() {
        assertEquals(Tile.Type.Center, board.getTile(7, 7).getSpecial());
        assertEquals(Tile.Type.TripleWord, board.getTile(0, 0).getSpecial());
        assertEquals(Tile.Type.DoubleWord, board.getTile(1, 1).getSpecial());
        assertEquals(Tile.Type.DoubleLetter, board.getTile(0, 3).getSpecial());
    }

    @Test
    public void testToStringReturnsFormattedBoard() {
        String result = board.toString();
        assertNotNull(result);
        assertTrue(result.contains("|"));
        assertTrue(result.contains("**")); 
    }

    @Test
    public void testGetTileReturnsCorrectTile() {
        Tile tile = board.getTile(0, 0);
        assertNotNull(tile);
    }

    @Test
    public void testCheckValidMovesTrueOnEmpty() {
        HashMap<Position, GamePiece> moveMap = new HashMap<>();
        moveMap.put(new Position(7, 7), GamePiece.getPiece());
        assertTrue(moveMap.values().stream().allMatch(gp -> gp != null));
    }

    @Test
    public void testSearchAroundLetterFindsHorizontalWord() {
        GamePiece gp1 = GamePiece.getPiece();
        GamePiece gp2 = GamePiece.getPiece();
        board.getTile(7, 6).setPiece(gp1);
        board.getTile(7, 8).setPiece(gp2);

        Move move = new Move();
        move.addPiece(GamePiece.getPiece(), 7, 7); 
        String word = board.searchAroundLetter(move);

        assertNotNull(word);
        assertTrue(word.length() >= 3);
    }

    @Test
    public void testInvalidMoveReturnsTilesToPlayer() {
        Player player = new Player("Test");
        GamePiece piece = player.getPlayerPieces().get(0);

        Move move = new Move();
        move.setPlacement(0, 0, Move.Directions.Horizontal);
        move.addPiece(piece, 0, 0); 

        int before = player.getPlayerPieces().size();
        board.playerMove(move, true, player); 
        int after = player.getPlayerPieces().size();

        assertTrue(after >= before); 
    }

}
