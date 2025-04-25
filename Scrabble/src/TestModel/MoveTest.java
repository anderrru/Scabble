package TestModel;

import Model.GamePiece;
import Model.Move;
import Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    private Move move;
    private GamePiece piece1;
    private GamePiece piece2;

    @BeforeEach
    public void setUp() {
        move = new Move();
        piece1 = GamePiece.getPiece();
        piece2 = GamePiece.getPiece();
    }

    @Test
    public void testAddAndGetAtPosition() {
        move.addPiece(piece1, 3, 5);
        GamePiece retrieved = move.getAtPosition(3, 5);
        assertEquals(piece1, retrieved);
    }
 
    @Test
    public void testClear() {
        move.addPiece(piece1, 2, 2);
        move.clear();
        assertEquals(0, move.getMove().size());
    }
    
    @Test
    public void testSetAndGetPlacement() {
        move.setPlacement(1, 1, Move.Directions.Horizontal);
        assertEquals(1, move.getX());
        assertEquals(1, move.getY());
        assertEquals(Move.Directions.Horizontal, move.getDirection());
    }
    
    @Test
    public void testGetDirectionFromAutoDetection() {
        move.addPiece(piece1, 2, 3);
        move.addPiece(piece2, 2, 4);
        assertEquals(Move.Directions.Vertical, move.getDirection());
    }
    
    @Test
    public void testGetStartPosition_Horizontal() {
        move.addPiece(piece1, 1, 4);
        move.addPiece(piece2, 3, 4);
        move.setDirection(Move.Directions.Horizontal);
        Position start = move.getStartPosition();
        assertEquals(1, start.getX());
        assertEquals(4, start.getY());
    }
    
    @Test
    public void testGetStartPosition_Vertical() {
        move.addPiece(piece1, 6, 2);
        move.addPiece(piece2, 6, 0);
        move.setDirection(Move.Directions.Vertical);
        Position start = move.getStartPosition();
        assertEquals(6, start.getX());
        assertEquals(0, start.getY());
    }
    
    @Test
    public void testGetPositionsSortedByX() {
        move.addPiece(piece1, 5, 3);
        move.addPiece(piece2, 2, 3);
        ArrayList<Position> sorted = move.getPositionsbyX();
        assertEquals(2, sorted.get(0).getX());
        assertEquals(5, sorted.get(1).getX());
    }
    
    @Test
    public void testGetPositionsSortedByY() {
        move.addPiece(piece1, 3, 5);
        move.addPiece(piece2, 3, 2);
        ArrayList<Position> sorted = move.getPositionsbyY();
        assertEquals(2, sorted.get(0).getY());
        assertEquals(5, sorted.get(1).getY());
    }
}
