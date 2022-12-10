package game.scrabble.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import game.scrabble.model.Coordinates;
import game.scrabble.model.Tile;

public class Algo {
    public int findNextMove(BoardManager bm, BoardGenerator bg, List<Coordinates> resultCoord,
        List<Integer> resultIndices, List<String> resultWords, List<Integer> resultScores) {
        Tile[] tiles = bm.currentPlayer().tiles;
        Tile[][] board = bm.tiles;
        char[][] chars = new char[bm.boardHeight][bm.boardWidth];

        Hashtable<Character, List<Coordinates>> h = new Hashtable<>();

        // first make a list of characters in current board
        for (int i = 0; i < bm.boardHeight; i++) {
            for (int j = 0; j < bm.boardWidth; j++) {
                if (board[i][j] == null) {
                    chars[i][j] = 0;
                    continue;
                }
                String txt = board[i][j].getText();
                if (txt.length() == 1) {
                    chars[i][j] = txt.charAt(0);
                } else
                    chars[i][j] = 0;
            }
        }

        // now create a hashtable with all the location of certain characters.
        for (int i = 0; i < bm.boardHeight; i++) {
            for (int j = 0; j < bm.boardWidth; j++) {
                char ch = chars[i][j];
                if (ch == 0)
                    continue;
                List<Coordinates> cd = new ArrayList<Coordinates>();
                if (h.containsKey(ch)) {
                    cd = h.get(ch);
                    h.remove(ch);
                }
                cd.add(new Coordinates(i, j));
                h.put(ch, cd);
                System.out.println("Added to h:" + ch + "(" + i + "," + j + ")");
            }
        }

        HashSet<String> words = bm.dictionaryManager.getWords();
        int[] charcnt = getCharCnt(tiles);
        List<String> maxWords = new ArrayList<String>();
        List<Integer> maxScores = new ArrayList<Integer>();
        List<Coordinates> maxCoord = new ArrayList<Coordinates>();
        List<Character> maxNeeded = new ArrayList<Character>();
        int maxScore = 0;
        for (String wordLow : words) {
            String word = wordLow.toUpperCase();
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                if (!h.containsKey(ch))
                    continue;

                List<Coordinates> cds = h.get(ch);
                for (Coordinates cd : cds) {

                    // check if can fit the word somewhere in board
                    int sx = cd.getX() - i;
                    if (sx < 0)
                        continue;
                    boolean found = true;
                    List<Character> needed = new ArrayList<Character>();
                    List<Coordinates> currentCoord = new ArrayList<Coordinates>();
                    for (int j = 0; j < word.length(); j++) {
                        if ((j + sx) >= bm.boardWidth) {
                            found = false;
                            break;
                        }
                        if (chars[cd.getY()][j + sx] != 0
                            && chars[cd.getY()][j + sx] != word.charAt(j)) {
                            found = false;
                            break;
                        }
                        if (chars[cd.getY()][j + sx] == 0) {
                            needed.add(word.charAt(j));
                            currentCoord.add(new Coordinates(cd.getY(), j + sx));
                        }
                    }
                    if (found && needed.size() > 0) {
                        // check if player has all needed tiles
                        boolean hasTiles = hasAllTilesNeeded(needed, charcnt);
                        if (hasTiles) {
                            char[][] tempcharr = copyArr2(chars, bm.boardHeight, bm.boardWidth);
                            for (int j = 0; j < word.length(); j++) {
                                tempcharr[cd.getY()][j + sx] = word.charAt(j);
                            }
                            if (Util.isBoardStillValid(tempcharr, bm.dictionaryManager, bm.boardHeight, bm.boardWidth)) {

                                List<String> outWords = new ArrayList<String>();
                                List<Integer> outScores = new ArrayList<Integer>();
                                int score = Util.calculateScore(bg, tempcharr, currentCoord,
                                    bm.dictionaryManager, outWords, outScores, bm.boardHeight, bm.boardWidth);
                                if (score > maxScore) {
                                    maxCoord = currentCoord;
                                    maxNeeded = needed;
                                    maxWords = outWords;
                                    maxScores = outScores;
                                    maxScore = score;
                                }
                            }
                        }
                    }

                    // NOW LOOK UP to DOWN

                    int sy = cd.getY() - i;
                    if (sy < 0)
                        continue;
                    found = true;
                    needed = new ArrayList<Character>();
                    currentCoord = new ArrayList<Coordinates>();
                    for (int j = 0; j < word.length(); j++) {
                        if ((j + sy) >= bm.boardHeight) {
                            found = false;
                            break;
                        }
                        if (chars[j + sy][cd.getX()] != 0
                            && chars[j + sy][cd.getX()] != word.charAt(j)) {
                            found = false;
                            break;
                        }
                        if (chars[j + sy][cd.getX()] == 0) {
                            needed.add(word.charAt(j));
                            currentCoord.add(new Coordinates(j + sy, cd.getX()));
                        }
                    }
                    if (found && needed.size() > 0) {
                        // check if player has all needed tiles
                        boolean hasTiles = hasAllTilesNeeded(needed, charcnt);
                        if (hasTiles) {

                            char[][] tempcharr = copyArr2(chars, bm.boardHeight, bm.boardWidth);
                            for (int j = 0; j < word.length(); j++) {
                                tempcharr[j + sy][cd.getX()] = word.charAt(j);
                            }
                            if (Util.isBoardStillValid(tempcharr, bm.dictionaryManager, bm.boardHeight, bm.boardWidth)) {

                                List<String> outWords = new ArrayList<String>();
                                List<Integer> outScores = new ArrayList<Integer>();
                                int score = Util.calculateScore(bg, tempcharr, currentCoord,
                                    bm.dictionaryManager, outWords, outScores, bm.boardHeight, bm.boardWidth);
                                if (score > maxScore) {
                                    maxCoord = currentCoord;
                                    maxNeeded = needed;
                                    maxWords = outWords;
                                    maxScores = outScores;
                                    maxScore = score;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        List<Integer> maxIndices = new ArrayList<Integer>();
        if (maxScore > 0) {
            boolean[] done = new boolean[tiles.length];
            for (int i = 0; i < maxNeeded.size(); i++) {

                for (int j = 0; j < tiles.length; j++) {
                    String text = tiles[j].getText();
                    
                    if (text.length() == 1 && text.charAt(0) == maxNeeded.get(i) && !done[j]) {
                        maxIndices.add(j);
                        done[j] = true;
                        break;
                    }
                }
            }
        }
        for (Coordinates cd : maxCoord)
            resultCoord.add(cd);
        for (int v : maxIndices)
            resultIndices.add(v);
        for (String w : maxWords)
            resultWords.add(w);
        for (int sc : maxScores)
            resultScores.add(sc);

        return maxScore;
    }

    private boolean hasAllTilesNeeded(List<Character> needed, int[] charcnt) {
        boolean hasTiles = true;

        int[] tempcnt = copyArr(charcnt);
        for (char ndChar : needed) {
            if (tempcnt[ndChar - 'A'] > 0) {
                tempcnt[ndChar - 'A']--;
            } else if (tempcnt[26] > 0)
                tempcnt[26]--;
            else {
                hasTiles = false;
                break;
            }
        }

        return hasTiles;
    }

    private int[] getCharCnt(Tile[] tiles) {
        int[] ret = new int[27];
        for (Tile t : tiles) {
            if (t.character == ' ')
                ret[26]++;
            else
                ret[t.character - 'A']++;
        }
        return ret;
    }

    private int[] copyArr(int[] arr) {
        int[] ret = new int[arr.length];
        for (int i = 0; i < arr.length; i++)
            ret[i] = arr[i];
        return ret;
    }

    private char[][] copyArr2(char[][] arr, int h, int w) {
        char[][] ret = new char[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                ret[i][j] = arr[i][j];
            }
        }
        return ret;
    }
}
