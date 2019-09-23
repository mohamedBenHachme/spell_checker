package spellChecker.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Node;

public class Trie {
	private TrieNode root;
	private int numOfWords;
	public Trie() {
		root = new TrieNode();
		root.setRoot(true);
		setNumberOfWords(0);
	}
	
    private void setNumberOfWords(int i) {
    	numOfWords = i;
	}

	public void insert(String word) {
        HashMap<Character, TrieNode> children = root.getChildren();
        TrieNode currentParent;
		currentParent = root;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode node;
            if(children.containsKey(c)) {
                node = children.get(c);
            } else { 
                node = new TrieNode(c);
                node.setParent(currentParent);
                children.put(c, node);
            }
            children = node.getChildren();
            currentParent = node;

            if(i == word.length() - 1) {
                node.setLeaf(true);
                this.numOfWords++;
            }
        }
    }
    
    public StringBuffer getMatchingPrefix(String input)  {
		StringBuffer buffer = new StringBuffer();
		TrieNode current = root;
		for(int i=0; i<input.length();i++){
			char ch = input.charAt(i);
			
			TrieNode node = current.getChildren().get(ch);
			if(node == null)
			{
				break;
			}
			buffer.append(ch);
			current = node;
		}
		
		return buffer;
	}
    
    public boolean search(String word) {
        HashMap<Character, TrieNode> children = root.getChildren();
        TrieNode node = null;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if(children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else { 
                node = null;
                break;
            }
        }

        if(node != null && node.isLeaf()) {
            return true;
        } else {
            return false;
        }
    }
    
    public int countMatchingPrefix(String prefix) {
    	if( !startsWith(prefix))
    		return 0;
    	return searchNode(prefix).getCount();
    }

	private boolean startsWith(String prefix) {
		if(searchNode(prefix) == null)
			return false;
		return true;
	}

	private TrieNode searchNode(String word) {
		HashMap<Character, TrieNode> children = root.getChildren();
        TrieNode node = null;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if(children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else { 
               return null;
            }
        }

        return node;
        
	}
    
	public ArrayList<String> similarity(String word, int maxDistance) {
		Map<String, Integer> results = getSimilarityMap(word, maxDistance);
		ArrayList<String> wordsList = new ArrayList<>();
		for(Map.Entry<String, Integer> element : results.entrySet()) {
			wordsList.add(element.getKey());
		}
		
		return  wordsList;
		
	}
	
	public Map<String, Integer> getSimilarityMap(String word, int maxDistance){
		Map<String, Integer> results = new HashMap<>();
		int length = word.length();
		
		Vector<Integer> currentRow = new Vector<Integer>(length + 1);
		for(int i = 0; i <= length; i++)
			currentRow.insertElementAt(i, i);
		for(Map.Entry<Character, TrieNode> entry : root.getChildren().entrySet())
			results.putAll(RecursiveLevenshteinDistance(entry.getValue(), entry.getValue().getCharacter(), word, currentRow,
                    results, maxDistance));
		return results;
	}
    
    public Map<String, Integer> RecursiveLevenshteinDistance(TrieNode node, char letter, String word,
    Vector<Integer> previousRow, Map<String, Integer> results, int maxDistance){
    	int columns = previousRow.size();
        Vector<Integer> currentRow = new Vector<Integer>(previousRow.size());
        currentRow.add(0, previousRow.get(0) + 1);
        int insertCost, deleteCost, replaceCost;
        for (int i = 1; i < columns; i++) {
            insertCost = currentRow.get(i - 1) + 1;
            deleteCost = previousRow.get(i) + 1;
            if (word.charAt(i - 1) != letter) {
                replaceCost = previousRow.get(i - 1) + 1;
            } else {
                replaceCost = previousRow.get(i - 1);
            }

            currentRow.add(i, Math.min(insertCost, Math.min(deleteCost, replaceCost)));
            //printVector(currentRow);
        }
        if (currentRow.lastElement() <= maxDistance && node.isLeaf()) {
            TrieNode currentParent = node.getParent();
            StringBuilder wordBuilder = new StringBuilder();
            while (currentParent != null) {
                if (currentParent.getParent() != null) {
                    wordBuilder.append(currentParent.getCharacter());
                }
                currentParent = currentParent.getParent();
            }
            results.put(wordBuilder.reverse().append(node.getCharacter()).toString(), currentRow.lastElement());
        }
        Object obj = Collections.min(currentRow);
        Integer i = new Integer((int) obj);
        if (i.intValue() <= maxDistance) {
            for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
                results.putAll(RecursiveLevenshteinDistance(entry.getValue(), entry.getValue().getCharacter(), word, currentRow,
                        results, maxDistance));
            }
        }

        return results;
    }
	
    
    
	
	
    
    
    
	
}
