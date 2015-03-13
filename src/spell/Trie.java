package spell;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class Trie implements ITrie {

	private int wordCount;
	private int nodeCount;
	private Node[] root;
	boolean FirstPass = true;
	TreeMap<String, Integer> suggestions2 = new TreeMap<String, Integer>();
	TreeSet<String> trieSet = new TreeSet<String>();
	
	public Trie()
	{
		wordCount = 0;
		nodeCount = 1; //root node
		root = new Node[26]; //size of 26 for letters in the alphabet
	}
	
	String suggestWord(String word) {
		String suggestedWord = "";
		INode foundNode;
		suggestedWord = suggestedWord(word);
		if (suggestedWord.equals("")) { // nothing is edit distance 1 away
			String possibleSuggestion = "";
			int highestFreq = 0;
			for (Map.Entry<String, Integer> treeMap2 : suggestions2.entrySet()) {
				FirstPass = false;
				possibleSuggestion = suggestedWord(treeMap2.getKey());
				if (!possibleSuggestion.equals("")) {
					foundNode = this.find(possibleSuggestion);
					if (foundNode.getValue() > highestFreq) {
						highestFreq = foundNode.getValue();
						suggestedWord = possibleSuggestion;
					}
				}
			}
		}
		return suggestedWord;
	}
	
	String suggestedWord(String word) {
		
		TreeMap<String, Integer> suggestions = new TreeMap<String, Integer>();
		String editedWord = "";
		StringBuilder sb = new StringBuilder();
		INode foundNode;
		//deletion distance
		for (int i = 0; i < word.length(); i++) {
			sb = new StringBuilder(word);
			editedWord = sb.deleteCharAt(i).toString();
			if (FirstPass && !editedWord.equals(""))
				suggestions2.put(editedWord,0);
			foundNode = this.find(editedWord);
			if (foundNode != null) { //edited word is in our Trie
				suggestions.put(editedWord, foundNode.getValue());
			}
		}
		
		//transposition distance
		char L = ' ';
		char R = ' ';
		for (int i = 0; i < word.length() - 1; i++) {
			sb = new StringBuilder(word);
			L = sb.charAt(i);
			R = sb.charAt(i + 1);
			sb.setCharAt(i, R);
			sb.setCharAt(i+1, L);
			if (FirstPass)
				suggestions2.put(sb.toString(),0);
			foundNode = this.find(sb.toString());
			if (foundNode != null) {//edited word is in our Trie
				suggestions.put(sb.toString(),foundNode.getValue());
			}
		}
		
		//Alteration Distance
		for (int i = 0; i < word.length(); i++) {
			for (int j = 0; j < 26; j++) {
				sb = new StringBuilder(word);
				char inputChar = (char) (j + 'a'); //inputChar matching original char doesn't matter because that would mean our original word is in the Trie, which we already checked for
				sb.setCharAt(i, inputChar);
				if (FirstPass)
					suggestions2.put(sb.toString(),0);
				foundNode = this.find(sb.toString());
				if (foundNode != null) { //edited word is in our Trie
					suggestions.put(sb.toString(), foundNode.getValue());
				}
			}
		}
		
		//Insertion Distance
		for (int i = 0; i <= word.length(); i++) {
			for (int j = 0; j < 26; j++) {
				sb = new StringBuilder(word);
				char inputChar = (char) (j + 'a');
				sb.insert(i, inputChar);
				if (FirstPass)
					suggestions2.put(sb.toString(),0);
				foundNode = this.find(sb.toString());
				if (foundNode != null) { //edited word is in our Trie
					suggestions.put(sb.toString(), foundNode.getValue());
				}
			}
		}
		
		int highestFreq = 0;
		String suggestion = "";
		//run through suggestions and find the "closest" one
		for(Entry<String, Integer> treeMap : suggestions.entrySet()) {
			if (treeMap.getValue() > highestFreq) {
				highestFreq = treeMap.getValue();
				suggestion = treeMap.getKey();
			}
		}
			
		
		return suggestion;
	}
	
	@Override
	public void add(String word) {

		String input = word.toLowerCase();
		char inputChar = input.charAt(0); //start at the first character
		
		if (this.root[inputChar - 'a'] == null) {
			this.root[inputChar - 'a'] = new Node();
			nodeCount++;
		}
		Node currentNode = this.root[inputChar - 'a'];
		for (int i = 1; i < input.length(); i++) {
			inputChar = input.charAt(i);
			
			if (currentNode.Nodes[inputChar - 'a'] == null) {
				currentNode.Nodes[inputChar-'a'] = new Node();
				nodeCount++;
			}
			currentNode = currentNode.Nodes[inputChar - 'a'];
		}
		
		if (currentNode.getValue() == 0) //this is a new word
			wordCount++;
		
		currentNode.frequency++; //increment frequency of this word
		trieSet.add(input);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String str : trieSet) {
			sb.append(str + "\n");
		}
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 10;
		result = prime * (result + nodeCount);
		result = prime * (result + wordCount);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trie other = (Trie) obj;
		if (nodeCount != other.nodeCount)
			return false;
		if (wordCount != other.wordCount)
			return false;
		return recursiveEquals(other.root, this.root);
	}
	
	private boolean recursiveEquals(Node[] root2, Node[] root3) {
		
		for (int i = 0; i < 26; i++) {
			if ((root2[i] == null) && (root3[i] == null))
				continue;
			else if ((root2[i].Nodes == null) && (root3[i].Nodes != null))
				return false;
			else if ((root2[i].Nodes != null) && (root3[i].Nodes == null))
				return false;
			else if (root2[i].getValue() != root3[i].getValue()) {	
				return false;
			}
			else if ((root2[i].Nodes == null) && (root3[i].Nodes == null))
				continue;
			else
				if (recursiveEquals(root2[i].Nodes, root3[i].Nodes)) {
					
				}
				else
					return false;
			
		}
		return true;
	}

		
	@Override
	public INode find(String word) {
		String input = word.toLowerCase();
		
		if (input.equals(""))
			return null;
		
		char inputChar = input.charAt(0);
		
		Node currentNode = this.root[inputChar - 'a'];
		
		if (this.root[inputChar - 'a'] == null)
			return null;
		
		for (int i = 1; i < input.length(); i++) {
			inputChar = input.charAt(i);
			if (currentNode.Nodes[inputChar - 'a'] == null)
				return null;
			currentNode = currentNode.Nodes[inputChar-'a'];			
			}
		
		//check for the final node
		if (currentNode.frequency == 0) { //this is a new word
			return null;
		} else {
			return currentNode;
		}
	}

	@Override
	public int getWordCount() {
		return wordCount;
	}

	@Override
	public int getNodeCount() {
		return nodeCount;
	}

}
