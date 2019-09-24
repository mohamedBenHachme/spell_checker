package spellChecker.controller;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import spellChecker.model.Trie;
import spellChecker.view.TextHighlighter;

public class SpellerController {
	static String PATH = "./src/words.all";
	Trie trie;
	HashMap<String, ArrayList<String>> suggestionListHashMap;
	
	public SpellerController() {
		this.trie = new Trie();
		this.suggestionListHashMap = new HashMap<String, ArrayList<String>>();
	}

	
	
	public Trie getTrie() {
		return trie;
	}
	public void setTrie(Trie trie) {
		this.trie = trie;
	}
	public HashMap<String, ArrayList<String>> getSuggestionListHashMap() {
		return suggestionListHashMap;
	}
	
	public void setSuggestionListHashMap(HashMap<String, ArrayList<String>> suggestionListHashMap) {
		this.suggestionListHashMap = suggestionListHashMap;
	}
	
	public void createDictionnary() {
		RandomAccessFile file = null;
		try {
        	file = new RandomAccessFile(PATH,"r"); }
        	catch (IOException ioe) { System.err.println("File not found"); }
        	try {
        	for (; ;) {
        	String word = file.readLine();
        	if (word == null) break;
        	if (word.indexOf((int)'\'')!= -1) continue;
        	trie.insert(word); }
        }
        	catch (EOFException eofe) { }
        	catch (IOException ioe) { ioe.printStackTrace(); }
		
	}
	
	public HashMap<String, ArrayList<String>> checkSpelling(JTextArea textArea, HashMap<String, ArrayList<String>> suggestionListHashMap, TextHighlighter highlight) {
		String[] words = textArea.getText().split(" ");
		 
		 for(String word : words) {
			 boolean isCorrecte = trie.search(word);
			 if(!isCorrecte) {
				ArrayList<String> list = trie.similarity(word, 1);
				suggestionListHashMap.put(word, list);
			 }
			 
		 }
		 
		 String[] list = suggestionListHashMap.keySet().toArray(new String[suggestionListHashMap.size()]);
		
		 highlight.highlight(textArea, list);
		 return suggestionListHashMap;
	}
	
	public void highlight(JTextArea textArea, String[] list, TextHighlighter highlight) {
		highlight.highlight(textArea, list);
	}
	
	
	public void fileExplorer(JFrame frame, JTextArea textArea) {
		JFileChooser j = new JFileChooser("D:"); 
  	  
        // Invoke the showsOpenDialog function to show the save dialog 
        int r = j.showOpenDialog(null); 

        // If the user selects a file 
        if (r == JFileChooser.APPROVE_OPTION) { 
            // Set the label to the path of the selected directory 
            File fi = new File(j.getSelectedFile().getAbsolutePath()); 

            try { 
                // String 
                String s1 = "", sl = ""; 

                // File reader 
                FileReader fr = new FileReader(fi); 

                // Buffered reader 
                @SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(fr); 

                // Initialize sl 
                sl = br.readLine(); 

                // Take the input from the file 
                while ((s1 = br.readLine()) != null) { 
                    sl = sl + "\n" + s1; 
                } 

                // Set the text 
               textArea.setText(sl); 
            } 
            catch (Exception evt) { 
                JOptionPane.showMessageDialog(frame, evt.getMessage()); 
            } 
        }
	
	}
	
	public void replaceAll(JTextArea textAreae, String oldWord, String newWord){
		textAreae.setText(textAreae.getText().toLowerCase().replaceAll(oldWord.toLowerCase(), newWord.toLowerCase()));
	}
}
