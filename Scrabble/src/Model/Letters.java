package Model;

import java.util.HashMap;
import java.util.Map;

/**
 * The Letters class stores the number of available tiles 
 * for each letter in a standard game of Scrabble.
 */
public class Letters {
	
    private final Map<Character, Integer> letterCounts = new HashMap<>();

    // Initializes letter frequencies
    public Letters() {
        addLetter('E', 12);
        addLetter('A', 9);
        addLetter('I', 9);
        addLetter('O', 8);
        addLetter('N', 6);
        addLetter('R', 6);
        addLetter('T', 6);
        addLetter('L', 4);
        addLetter('S', 4);
        addLetter('U', 4);
        addLetter('D', 4);
        addLetter('G', 3);
        addLetter('B', 2);
        addLetter('C', 2);
        addLetter('M', 2);
        addLetter('P', 2);
        addLetter('F', 2);
        addLetter('H', 2);
        addLetter('V', 2);
        addLetter('W', 2);
        addLetter('Y', 2);
        addLetter('K', 1);
        addLetter('J', 1);
        addLetter('X', 1);
        addLetter('Q', 1);
        addLetter('Z', 1);
    }

    // Adds a letter and its count to the map
    private void addLetter(char letter, int count) {
        letterCounts.put(Character.toUpperCase(letter), count);
    }

    // Returns number of times a letter appears
    public int getLetterCount(char letter) {
        return letterCounts.getOrDefault(Character.toUpperCase(letter), 0);
    }

    // Returns the full map of letter count
    public Map<Character, Integer> getLetterCounts() {
        return letterCounts;
    }
}
