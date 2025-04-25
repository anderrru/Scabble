package TestModel;

import Model.GamePiece;
import Model.Letter;
import Model.PointValue;
import Model.Tile;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

public class TileTests {

    private GamePiece createMockPiece(Letter letter, PointValue value) throws Exception {
        Constructor<GamePiece> constructor = GamePiece.class.getDeclaredConstructor(Letter.class, PointValue.class);
        constructor.setAccessible(true);
        return constructor.newInstance(letter, value);
    }

    @Test
    public void testEmptyTileInitially() {
        Tile tile = new Tile();
        assertTrue(tile.isEmpty());
        assertNull(tile.getPiece());
        assertNull(tile.getSpecial());
    }

    @Test
    public void testSetAndGetSpecialType() {
        Tile tile = new Tile();
        tile.setSpecial(Tile.Type.DoubleLetter);

        assertEquals(Tile.Type.DoubleLetter, tile.getSpecial());
    }

    @Test
    public void testSetPieceAndToStringLetter() throws Exception {
        Tile tile = new Tile();
        GamePiece mock = createMockPiece(Letter.Z, PointValue.Ten);
        tile.setPiece(mock);

        assertFalse(tile.isEmpty());
        assertEquals("Z ", tile.toString());
    }

    @Test
    public void testToStringWithSpecials() {
        Tile center = new Tile();
        center.setSpecial(Tile.Type.Center);
        assertEquals("**", center.toString());

        Tile tripleW = new Tile();
        tripleW.setSpecial(Tile.Type.TripleWord);
        assertEquals("TW", tripleW.toString());

        Tile tripleL = new Tile();
        tripleL.setSpecial(Tile.Type.TripleLetter);
        assertEquals("TL", tripleL.toString());

        Tile doubleW = new Tile();
        doubleW.setSpecial(Tile.Type.DoubleWord);
        assertEquals("DW", doubleW.toString());

        Tile doubleL = new Tile();
        doubleL.setSpecial(Tile.Type.DoubleLetter);
        assertEquals("DL", doubleL.toString());

        Tile blank = new Tile();
        blank.setSpecial(Tile.Type.Blank);
        assertEquals("  ", blank.toString());
    }
}
