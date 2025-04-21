package tests;

import Model.WordMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WordMapTests {

    @Test
    public void testWordMapLoads() {
        WordMap wordMap = new WordMap();
        assertNotNull(wordMap.getWordMap());
        assertTrue(wordMap.getMapSize() > 0); 
    }

    @Test
    public void testContainsKnownWord() {
        WordMap wordMap = new WordMap();
        String word = "ABAND"; 
        int hash = word.hashCode();
        assertEquals(word, wordMap.getWord(hash));
    }

    @Test
    public void testInvalidWordReturnsNull() {
        WordMap wordMap = new WordMap();
        int invalidHash = "ZZZZZ".hashCode(); 
        assertNull(wordMap.getWord(invalidHash));
    }

    @Test
    public void testMapSizeIsStable() {
        WordMap wordMap1 = new WordMap();
        WordMap wordMap2 = new WordMap();
        assertEquals(wordMap1.getMapSize(), wordMap2.getMapSize());
    }
}
