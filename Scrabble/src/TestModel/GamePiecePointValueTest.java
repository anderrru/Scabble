package tests;

import Model.GamePiece;
import Model.Letter;
import Model.PointValue;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

public class GamePiecePointValueTest {

    private GamePiece createGamePiece(Letter letter, PointValue value) throws Exception {
        Constructor<GamePiece> constructor = GamePiece.class.getDeclaredConstructor(Letter.class, PointValue.class);
        constructor.setAccessible(true);
        return constructor.newInstance(letter, value);
    }

    @Test
    public void testPointValueMapping() throws Exception {
        assertEquals(1, createGamePiece(Letter.A, PointValue.One).getPointValue());
        assertEquals(2, createGamePiece(Letter.D, PointValue.Two).getPointValue());
        assertEquals(3, createGamePiece(Letter.C, PointValue.Three).getPointValue());
        assertEquals(4, createGamePiece(Letter.F, PointValue.Four).getPointValue());
        assertEquals(5, createGamePiece(Letter.K, PointValue.Five).getPointValue());
        assertEquals(8, createGamePiece(Letter.X, PointValue.Eight).getPointValue());
        assertEquals(10, createGamePiece(Letter.Z, PointValue.Ten).getPointValue());
    }
}

