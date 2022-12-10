package game.scrabble.utils;

import java.util.ArrayList;

public class DictionaryUtil {
    private boolean devMode = false;

    public DictionaryUtil(boolean devMode) {
        this.devMode = devMode;
    }

    public static ArrayList<String> makeList(){
        ArrayList<String> names = new ArrayList<String>();
        names.add("John");
        names.add("Jim");
        names.add("Bob");
        names.add("Austin");
        names.add("Joe");
        names.add("Alex");
        names.add("Linus");
        names.add("Tom");
        names.add("Derek");
        names.add("Chris");
        return names;
    }

    public boolean isDev(){
        return this.devMode;
    }

    /**
     * The following method is to print a dev message
     * @param message {String} - The message to print
     */
    public void printDev(String message) {
        if (this.devMode) {
            System.out.println("[DEV] " + message);
        }
    }
}
