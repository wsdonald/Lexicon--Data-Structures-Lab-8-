import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

//Problems - File/Scrabble dictionary not working despite things working on smaller scale (why?)
//Could also be a problem with my addWord or addChild which I thought were solid. Could also be a problem with contains
//What's the dealyo

/**
 * LexiconTrie
 * 
 * Implements Lexicon interface, by executing all of its methods.
 * Stores nodes in a structure, from which they can be accessed as words, prefixes regexs and spelling corrections
 * 
 * @author WS Donaldson
 */

public class LexiconTrie implements Lexicon{
    private static int totalWords;
    private LexiconNode root = new LexiconNode(' ');

    public LexiconTrie() {

    }

    /**
     * addWord
     * Takes a string and adds it to the lexicon
     * 
     * @param str - string which is to be added
     * @return boolean, returns true if it is a new word for the Lexicon, false if it is already there
     */
    public boolean addWord(String str) {
        LexiconNode current = root;

        if (this.containsWord(str)) {
            return false;
        }
        else {
            for (int i = 0; i < str.length(); i++) {
                if (current.getChild(str.charAt(i)) == null) {
                    current.addChild(str.charAt(i));
                    current = current.getChild(str.charAt(i));
                }
                else {
                    current = current.getChild(str.charAt(i));
                }
            }
            current.isWord(true);
        }       
        totalWords++;
        return true;
    }

    /**
     * addWordsFromFile
     * Takes a file in which all the words are seperated by line, all all lower case, 
     * then adds each of the words to the Lexicon
     * 
     * @param filename - the name of the file to be added
     * @return int - the number of words in the file
     */
    public int addWordsFromFile(String filename) {
        int result = 0;
        String file = readFileAsString(filename);
        String [] words = file.split("\n");
        for (int i = 0; i < words.length; i ++) {
            String current = words[i].trim();
            addWord(current);
            result ++;
        }
        return result;
    }

    /**
     * removeWord
     * Takes an already present word out of the lexicon
     * 
     * @param str - the word to be removed
     * @return boolean - false if the word isn't there, true otherwise.
     */
    public boolean removeWord(String str){
        LexiconNode current = root;
        if (! this.containsWord(str)) {
            return false;
        }
        else {
            for (int i = 0; i < str.length(); i++) {
                current = current.getChild(str.charAt(i));
            }
            current.isWord(false);
            totalWords --;
        }
        return true;
    }

    /**
     * numWords
     * Gives the total number of words
     * 
     * @return int - the total number of words
     */
    public int numWords(){
        return totalWords;

    }

    /**
     * containsWord
     * Checks to see if a certain word is already present in the lexicon.
     * 
     * @param str - the word to be checked
     * @return boolean - true if it is there, false otherwise.
     */
    public boolean containsWord(String str) {
        LexiconNode current = root;
        for (int i = 0; i < str.length(); i++) {
            if (current.getChild(str.charAt(i)) != null) {
                current = current.getChild(str.charAt(i));
            }
            else {
                return false;
            }
        }
        if (current.isWord()) {
            return true;
        }
        return false;
    }

    /**
     * containsPrefix
     * checks to see if a prefix is part of the lexicon
     * 
     * @param prefix - the prefix to be checked
     * @return boolean - true if it is there, false otherwise.
     */
    public boolean containsPrefix(String prefix) {
        LexiconNode current = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (current.getChild(prefix.charAt(i)) != null) {
                current = current.getChild(prefix.charAt(i));
            }
            else {
                return false;

            }
        }
        return true;

    }

    /**
     * Iterator
     * Calls a recursive helper method and returns an iterator over a list of all the words in the lexicon.
     * 
     * @return Iterator<String> is the iterator which is returned
     */
    public Iterator<String> iterator() {
        ArrayList<String> words = new ArrayList<String>();
        buildList("", root,  words);

        return words.iterator();

    }

    /**
     * buildList
     * The helper method for Iterator, finds all of the words and adds them to a list.
     * 
     * @param prefix - builds up the path taken to the final node in the word
     * @param current- the current node being examined
     * @param words - the list which is added to
     */
    private void buildList(String prefix, LexiconNode current, ArrayList<String> words) {
        prefix += current.getValue();
        if (current.getChild(0) == null) {
            if (current.isWord()) {
                words.add(prefix);
            }
        }
        else {
            if (current.isWord()) {
                words.add(prefix);
                for (LexiconNode child : current) {
                    buildList(prefix, child, words);
                }
            }
            else{
                for (LexiconNode child : current) {
                    buildList(prefix, child, words);
                }
            }
        }
    }

    /**
     * suggestCorrections
     * calls a recursive helper method and returns a list of possible corrections to a word
     * 
     * @param target - the word to be corrected
     * @param maxDistance - the number of changes from the target one can make
     * 
     * @return Set<String> - The set of possible corrections
     */
    public Set<String> suggestCorrections(String target, int maxDistance) {
        Set<String> corrections = new HashSet<String>();
        buildCorrections(target, maxDistance, corrections, "", root);
        return corrections;
    }

    /**
     * buildCorrections
     * the helper method for suggestCorrections - builds a list of all possible corrections to a target/maxdistance combo
     * 
     * @param target - the word to be corrected
     * @param flexibility - the number of changes which are still allowed to the target
     * @param corrections - the list of words 
     * @param prefix - builds up the path taken to the final node in the word
     * @param current - the node that is currently being examined.
     */
    private void buildCorrections(String target, int flexibility, Set<String> corrections, String prefix, LexiconNode current) {
        prefix += current.getValue();
        if (flexibility < 0) {
            //Stop the recursive madness
        }
        else if (target.length() == 0){
            if (current.isWord()) {
                System.out.println("target.length() == 0 clause activated");
                corrections.add(prefix);
            }
        }
        else {
            for (LexiconNode child: current) {
                if (child.getValue() == (target.charAt(0))) { 
                    buildCorrections(target.substring(1), flexibility, corrections, prefix, child);
                }
                else {
                    buildCorrections(target.substring(1), flexibility - 1, corrections, prefix, child);
                }
            }
        }
    }

    /**
     * matchRegex
     * Calls a recursive helper method, then returns matches on a regex pattern
     * 
     * @param pattern - the regex
     * @return Set<String> - the matches
     */
    public Set<String> matchRegex(String pattern) {
        Set<String> matches = new HashSet<String>();
        buildMatches(pattern,  matches, "", root);
        return matches;
    }

    /**
     * buildMatches
     * The recursive helper method for matchRegex - builds a list of all matches
     * 
     * @param pattern - the regex pattern
     * @param matches -the list which is added to over time
     * @param prefix - builds up the path to the final node in a word
     * @param current - the node which is being examined at present (starts as the root)
     */
    private void buildMatches(String pattern, Set<String> matches, String prefix, LexiconNode current) {
        prefix += current.getValue();
        if (pattern.length() == 0) {
            if (current.isWord()) {
                matches.add(prefix);
            }
        }

        else {
            if (pattern.charAt(0) == '_') {
                for (LexiconNode child: current) {
                    buildMatches(pattern.substring(1), matches, prefix, child);
                }
            }
            else if (pattern.charAt(0) == '?') {
                buildMatches(pattern.substring(1), matches, prefix.substring(0, prefix.length()-1), current); // Tries ? = ''
                for (LexiconNode child: current) {
                    buildMatches(pattern.substring(1), matches, prefix, child);
                }
            }
            else if (pattern.charAt(0) == '*') {
                if (current.getChild(0) != null) {
                    for (LexiconNode child : current) {
                        buildMatches(pattern.substring(1), matches, prefix, child); 
                        buildMatches(pattern, matches, prefix, child); 
                    }
                }
                buildMatches(pattern.substring(1), matches, prefix.substring(0, prefix.length()-1), current);
            }
            else {
                for (LexiconNode child: current) {
                    if (child.getValue() == (pattern.charAt(0))) {
                        buildMatches(pattern.substring(1), matches, prefix, child);
                    }
                }
            }
        }
    }

    /**
     * Reads the user-chosen file into a string. If the file doesn't exist it returns null
     * 
     * @param filename is the name of the file
     * @return the file as string
     * 
     * Credit to Sean Barker for providing the code
     */
    private static String readFileAsString(String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            return null;
        }
    }
}
