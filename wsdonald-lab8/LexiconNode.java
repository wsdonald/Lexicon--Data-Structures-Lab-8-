import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * LexiconNode
 * Each node is a component of the trie
 * These nodes are then combined into words and prefixes in allignment with the Lexicon interface.
 * 
 * @author WS Donaldson
 */
public class LexiconNode implements Iterable<LexiconNode> {
    private ArrayList<LexiconNode> children = new ArrayList<LexiconNode>(); 
    private char value;
    private boolean finality = false;
    private int childrenNum;
    
    /**
     * Constructor
     * @param value - the character to be stored
     */
    public LexiconNode(char value){
        this.value = value;
    }
    
    /**
     * getValue
     * gets the char
     * 
     * @return char - the char
     */
    public char getValue(){
        return this.value;
    }
    
    /**
     * Add child by character
     * Takes a char, makes a new node, and adds it as a child to the node under consideration
     * 
     * @param c - the character to be added
     * @return boolean - I'm basically using this as a break here. Originally I used the boolean to indicate 
     * that the node could actually be added (that is, the method would return false if the char in question was already a child)
     * but upon Prof. Barker's suggestion, I outsourced that distinction to the LexiconTrie
     */
    public boolean addChild(char c) { 

        for (int i = 0; i < this.children.size(); i++) {
            if (c < children.get(i).getValue()) {
                children.add(i, new LexiconNode(c));
                childrenNum++;
                return true;
            }

        }
        children.add(new LexiconNode(c));
        childrenNum++;
        return true;
    }
    


    /**
     * Gets child by index
     * 
     * @param index - the index at which to look for a child
     * @return LexiconNode - the child to be returned
     */
    public LexiconNode getChild(int index) {
        if (index < this.children.size()) {
            return children.get(index);
        }
        return null;
    }
    
    /**
     * Gets child by character
     * 
     * @param c - the character of the child we want to get
     * @return LexiconNode - the child to be returned
     */
    public LexiconNode getChild(char c) {
        for (LexiconNode child : this.children) { //For each child
                if (child.getValue() == c) {
                    return child;
                }
        }
        return null;
    }
    
    /**
     * isWord 
     * Sets the boolean finality, to mark if the node in question is the final letter in some word or not
     * 
     * @param b - the boolean we want to assign
     */
    public void isWord(boolean b) {
        this.finality = b;
    }
    
    /**
     * Tests the node to see if it is the final letter in some word
     * 
     * @return boolean - true if it is, false otherwise
     */
    public boolean isWord() {
        return finality;
    }
    
    /**
     * toString
     * 
     * @return String - the value (a char) inside of the node
     */
    public String toString() {
        return String.valueOf(this.getValue());
    }
    
    /**
     * iterator
     * 
     * @return Iterato<LexiconNode> - returns the standard list iterator but over all the children of the node in question
     */
    public Iterator<LexiconNode> iterator() {
        return this.children.iterator();
    }
    /**
     * removeChild
     * @param c - the char of the child to be removed
     */
    public void removeChild(char c) {
        children.remove(this.getChild(c));
    }
    /**
     * totalChildren
     * 
     * @return int - the number of children
     */
    public int totalChildren() {
        return childrenNum;
    }
}

