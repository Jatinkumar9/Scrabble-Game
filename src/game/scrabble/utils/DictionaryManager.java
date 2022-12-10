package game.scrabble.utils;
// The following class is for managing the dictionary and what is acceptable.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class DictionaryManager {
    // Instance variables
    private HashSet<String> words = new HashSet<String>();
    private DictionaryUtil dictionaryUtil;

    /**
     * The following method is used to load the dictionary into the program.
     */
    public DictionaryManager(DictionaryUtil dev) {
        // Load the dictionary
        this.loadDictonary();
        this.dictionaryUtil = dev;
    }

    /**
     * The following method is used to load the dictionary into the program.
     * This overloaded constructor is used to load a custom dictionary.
     * @param {ArrayList} - The array of words to load into the dictionary.
     */
    public DictionaryManager(HashSet<String> words, DictionaryUtil dev) {
        this.words = words;
        this.dictionaryUtil = dev;
    }

    /**
     * The following method is used to load the dictionary into the program.
     * It will use the default directory defined as path.
     */
    private void loadDictonary() {
        try {
            File myObj = new File(Constants.PATH);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                this.words.add(data);
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * The following getter is for getting all the words in the dictionary.
     * @return {ArrayList} - Returns the array of words in the dictionary.
     */
    public HashSet<String> getWords() {
        return this.words;
    }

    /**
     * isWord is used for checking if the word is valid.
     * @param word {String} The word to search for.
     * @return {boolean} True if the word is found, false otherwise.
     */
    public boolean isWord(String word) {
        word = word.toLowerCase();
        this.dictionaryUtil.printDev("Searching for word: " + word + " in list of " + this.words.size());
        return this.words.contains(word);
    }
}