package spell;

import java.io.*;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector {

	Trie dictionary = new Trie();
	
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		
		FileReader FR = new FileReader(dictionaryFileName);
		

		Scanner scanner;
		scanner = new Scanner(FR);
		
		while (scanner.hasNext()) {
			String temp = scanner.next();
			dictionary.add(temp);
		}
		
		
		
		scanner.close();
		
	}

	@Override
	public String suggestSimilarWord(String inputWord)throws NoSimilarWordFoundException {
		
		/* For TESTING
		Scanner input;
		input = new Scanner(System.in);
		if (dictionary.find(input.next()) == null)
			System.out.println("WORD NOT FOUND");
		else
			System.out.println("FOUND");	
		input.close();
		*/
		String word = "";
		word = inputWord.toLowerCase();
		
		if (dictionary.find(word) != null) {
			return word;
		}
		
		String suggestedWord = "";
		suggestedWord = dictionary.suggestWord(word);
		if (suggestedWord.equals(""))
			throw new NoSimilarWordFoundException();
		
		return suggestedWord;
	}

}
