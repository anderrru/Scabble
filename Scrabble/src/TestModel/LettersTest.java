package TestModel;

import org.junit.jupiter.api.Test;

import Model.Letters;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LettersTest {

    @Test
    public void testLetterFrequencies() {
        Letters letters = new Letters();

        assertEquals(12, letters.getLetterCount('E'));
        assertEquals(9, letters.getLetterCount('A'));
        assertEquals(1, letters.getLetterCount('J'));
        assertEquals(0, letters.getLetterCount('?')); 
    }

    @Test
    public void testCaseInsensitivity() {
        Letters letters = new Letters();

        assertEquals(12, letters.getLetterCount('e')); 
        assertEquals(12, letters.getLetterCount('E')); 
        assertEquals(3, letters.getLetterCount('g'));
        assertEquals(3, letters.getLetterCount('G'));
    }

    @Test
    public void testFullMapContainsAllLetters() {
        Letters letters = new Letters();
        Map<Character, Integer> map = letters.getLetterCounts();

        assertEquals(26, map.size()); 
        assertTrue(map.containsKey('B'));
        assertTrue(map.containsKey('H'));
        assertFalse(map.containsKey('@'));
    }
}
