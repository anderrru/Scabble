package tests;

import Model.Position;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testGetXAndY() {
        Position pos = new Position(5, 10);
        assertEquals(5, pos.getX());
        assertEquals(10, pos.getY());
    }

    @Test
    public void testSortByX_LessThan() {
        Position p1 = new Position(1, 5);
        Position p2 = new Position(3, 2);
        Comparator<Position> comp = Position.sortByXComparator();
        assertTrue(comp.compare(p1, p2) < 0);
    }

    @Test
    public void testSortByX_GreaterThan() {
        Position p1 = new Position(4, 2);
        Position p2 = new Position(2, 9);
        Comparator<Position> comp = Position.sortByXComparator();
        assertTrue(comp.compare(p1, p2) > 0);
    }

    @Test
    public void testSortByX_TiebreakerY() {
        Position p1 = new Position(2, 3);
        Position p2 = new Position(2, 5);
        Comparator<Position> comp = Position.sortByXComparator();
        assertTrue(comp.compare(p1, p2) < 0);
    }

    @Test
    public void testSortByY_LessThan() {
        Position p1 = new Position(4, 1);
        Position p2 = new Position(2, 3);
        Comparator<Position> comp = Position.sortByYComparator();
        assertTrue(comp.compare(p1, p2) < 0);
    }

    @Test
    public void testSortByY_GreaterThan() {
        Position p1 = new Position(2, 7);
        Position p2 = new Position(5, 4);
        Comparator<Position> comp = Position.sortByYComparator();
        assertTrue(comp.compare(p1, p2) > 0);
    }

    @Test
    public void testSortByY_TiebreakerX() {
        Position p1 = new Position(1, 5);
        Position p2 = new Position(3, 5);
        Comparator<Position> comp = Position.sortByYComparator();
        assertTrue(comp.compare(p1, p2) < 0);
    }

    @Test
    public void testSortingListByXComparator() {
        List<Position> list = Arrays.asList(
            new Position(3, 1),
            new Position(1, 4),
            new Position(2, 3),
            new Position(2, 2)
        );
        Collections.sort(list, Position.sortByXComparator());

        assertEquals(1, list.get(0).getX());
        assertEquals(2, list.get(1).getX());
        assertEquals(2, list.get(2).getX());
        assertEquals(3, list.get(3).getX());

        assertTrue(list.get(1).getY() < list.get(2).getY());
    }

    @Test
    public void testSortingListByYComparator() {
        List<Position> list = Arrays.asList(
            new Position(4, 3),
            new Position(2, 1),
            new Position(5, 1),
            new Position(1, 5)
        );
        Collections.sort(list, Position.sortByYComparator());

        assertEquals(1, list.get(0).getY());
        assertEquals(1, list.get(1).getY());
        assertEquals(3, list.get(2).getY());
        assertEquals(5, list.get(3).getY());

        assertTrue(list.get(0).getX() < list.get(1).getX());
    }
}
