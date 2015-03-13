package spell;

import java.io.FileNotFoundException;
import java.io.IOException;

import spell.ISpellCorrector.NoSimilarWordFoundException;

public class Main {

	public static void main (String[] args) throws FileNotFoundException {

		SpellCorrector SpellChecker = new SpellCorrector();
		String suggestion = null;
		try {
			SpellChecker.useDictionary(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			suggestion = SpellChecker.suggestSimilarWord(args[1]);
		} catch (NoSimilarWordFoundException e) {
			e.printStackTrace();
		}		
		
		System.out.println("suggestion: " + suggestion);
		
		return;
	}
}
