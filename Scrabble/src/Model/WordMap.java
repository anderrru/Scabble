/*
* Authors: Johnathan Alexander, Erik Picazzo, Andrew Wong, Andrew Huynh
*
* Description: This file creates a HashMap of all of the legal words
*		in the Scrabble dictionary (Collins 2019 edition), utilizing
*		their hash code values for quicker searching. Words are placed
*		into the HashMap in the form <word hashCode, word String>. 
*/

package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class WordMap {

	private HashMap<Integer, String> wordMap;

	// Constructor
	public WordMap(){
		try {
			wordMap = makeWordMap();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static HashMap<Integer, String> makeWordMap() throws FileNotFoundException {
		/* 
  		* This method creates the HashMap of the legal words in the Collins 2019
		* Scrabble dictionary, from the text file "Collins Scrabble Words (2019).txt"
		*/
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

		wordScanner.close();
		return wordMap;
	}

	public HashMap<Integer, String> getWordMap() {
		// This method returns the HashMap of wordMap
		return wordMap;
	}

	public String getWord(int wordCode) {
		// This method returns the word in wordMap based off the argument hash code, if it exists in the HashMap
		return wordMap.get(wordCode);
	}
	
	public int getMapSize() {
		// This method returns the size of the HashMap
		return wordMap.size();
	}
}
