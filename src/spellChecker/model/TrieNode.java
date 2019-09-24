package spellChecker.model;

import java.util.HashMap;

public class TrieNode {
	private char character;
    private HashMap<Character, TrieNode> children = new HashMap<>();
    private boolean isLeaf;
    private boolean isRoot;
    private boolean isVisited;
    private int count;
    private TrieNode parent;
    public TrieNode() {
    	setCount(0);
        setVisited(false);
    }

    public TrieNode(char c){
    	this();
        this.character = c;
    }

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public void setChildren(HashMap<Character, TrieNode> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public TrieNode getParent() {
		return parent;
	}

	public void setParent(TrieNode parent) {
		this.parent = parent;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public int getCount() {
		return count;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
	
	
	
}
