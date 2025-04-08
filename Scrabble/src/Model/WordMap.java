package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class WordMap {

	private HashMap<Integer, String> wordMap;
	
	public WordMap() throws FileNotFoundException {
		wordMap = makeWordMap();
	}
	
	private static HashMap<Integer, String> makeWordMap() throws FileNotFoundException {
		File wordFile = new File("Collins Scrabble Words (2019).txt");
		Scanner wordScanner = new Scanner(wordFile);
		wordScanner.nextLine();
		wordScanner.nextLine();
		
		HashMap<Integer, String> wordMap = new HashMap<Integer, String>();
		
		while (wordScanner.hasNextLine()) {
			String word = wordScanner.nextLine().trim();
			int wordCode = word.hashCode();
			wordMap.put(wordCode, word);
		}
		
		return wordMap;
	}

	public HashMap<Integer, String> getWordMap() {
		return wordMap;
	}
