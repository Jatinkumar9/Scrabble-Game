package game.scrabble.utils;

import java.awt.Color;
import java.util.List;

import javax.swing.border.LineBorder;

import game.scrabble.model.CellType;
import game.scrabble.model.Coordinates;
import game.scrabble.model.ScrabbleCellButton;
import game.scrabble.model.TileBag;

public class Util {
    public static String getCellTypeString(CellType type) {
        switch (type) {
        case NORMAL:
            return "";
        case DOUBLE_LETTER:
            return "DL";
        case DOUBLE_WORD:
            return "DW";
        case STAR:
            return "**";
        case TRIPLE_LETTER:
            return "TL";
        case TRIPLE_WORD:
            return "TW";
        default:
            return "";
        }
    }

    public static Color getCellColor(BoardGenerator bg, int i, int j) {
        CellType type = bg.cellTypes[i][j];
        switch (type) {
        case NORMAL:
            return new Color(255, 250, 240);
        case DOUBLE_LETTER:
            return Color.CYAN;
        case DOUBLE_WORD:
            return Color.PINK;
        case STAR:
            return Color.red;
        case TRIPLE_LETTER:
            return new Color(100, 120, 255);
        case TRIPLE_WORD:
            return Color.orange;
        default:
            return Color.yellow;
        }

    }

    public static void setupButtonTextColor(BoardGenerator bg, ScrabbleCellButton button, boolean enabled) {
        if (button.getText().length() == 2 && enabled)
            button.setForeground(getCellColor(bg, button.getSelf().getY(), button.getSelf().getX()));

        // if (button.getText().length() == 1 || (!enabled)) {
        if (button.getText().length() == 1) {
            button.setForeground(Color.BLACK);
            button.setBackground(Color.YELLOW);
        } else {
            button.setBackground(getCellColor(bg, button.getSelf().getY(), button.getSelf().getX()));
        }

    }

    public static void displayEnabledButton(BoardGenerator bg, ScrabbleCellButton button, boolean enabled) {
        button.setEnabled(enabled);
        if (enabled)
            button.setBorder(new LineBorder(Color.BLUE, 3));
        else
            button.setBorder(new LineBorder(Color.black, 1));

        setupButtonTextColor(bg, button, enabled);
    }

    public static boolean isBoardStillValid(ScrabbleCellButton[][] cells, DictionaryManager dict,
        int height, int width) {
        char[][] chars = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String t = cells[i][j].getText();
                if (t.length() != 1) {
                    chars[i][j] = 0;
                } else {
                    chars[i][j] = t.charAt(0);
                }
            }
        }
        return isBoardStillValid(chars, dict, height, width);
    }

    public static boolean isBoardStillValid(char[][] chars, DictionaryManager dict, int height,
        int width) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (chars[i][j] == 0)
                    continue;

                // look if there is a valid word left to right
                if (j == 0 || chars[i][j - 1] == 0) {
                    String curr = chars[i][j] + "";
                    for (int k = j + 1; k < width; k++) {
                        if (chars[i][k] != 0) {
                            curr += (chars[i][k] + "");
                        } else {
                            break;
                        }
                    }
                    if (curr.length() > 1 && !dict.isWord(curr)) {
                        return false;
                    }
                }

                // check if valid word top to bottom
                if (i == 0 || chars[i - 1][j] == 0) {
                    String curr = chars[i][j] + "";
                    for (int k = i + 1; k < height; k++) {
                        if (chars[k][j] != 0) {
                            curr += (chars[k][j] + "");
                        } else {
                            break;
                        }
                    }
                    if (curr.length() > 1 && !dict.isWord(curr)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isInCoordinates(List<Coordinates> coord, int y, int x) {
        for (Coordinates c : coord) {
            if (c.getY() == y && c.getX() == x)
                return true;
        }
        return false;
    }

    public static int calculateScore(BoardGenerator bg, ScrabbleCellButton[][] cells,
        List<Coordinates> currentCoordinates, DictionaryManager dict, List<String> outWords,
        List<Integer> outScores, int height, int width) {
        char[][] chars = getChars(cells, height, width);
        return calculateScore(bg, chars, currentCoordinates, dict, outWords, outScores, height, width);
    }

    public static int calculateScore(BoardGenerator bg, char[][] chars, List<Coordinates> currentCoordinates,
        DictionaryManager dict, List<String> outWords, List<Integer> outScores, int height,
        int width) {
        int ret = 0;
        for (Coordinates cd : currentCoordinates) {
            String str = "";
            int startX = cd.getX();
            boolean isCont = false;
            for (int i = cd.getX() - 1; i >= 0; i--) {
                if (isInCoordinates(currentCoordinates, cd.getY(), i)) {
                    isCont = true;
                    break;
                }
                char ch = chars[cd.getY()][i];
                if (ch == 0)
                    break;
                startX = i;
                str = ch + str;
            }
            if (isCont)
                continue;

            // look for word left to right
            for (int i = cd.getX(); i < width; i++) {
                char ch = chars[cd.getY()][i];
                if (ch != 0) {
                    str += ch;
                    if (str.length() > 1 && dict.isWord(str)) {
                        int wordScore = 0;
                        int mult = 1;
                        for (int k = startX; k < startX + str.length(); k++) {
                            char ch2 = chars[cd.getY()][k];
                            int letterScore = TileBag.getScore(ch2);
                            if (isInCoordinates(currentCoordinates, cd.getY(), k)) {
                                CellType tp = bg.cellTypes[cd.getY()][k];
                                if (tp == CellType.DOUBLE_LETTER)
                                    letterScore *= 2;
                                else if (tp == CellType.TRIPLE_LETTER)
                                    letterScore *= 3;
                                if (tp == CellType.TRIPLE_WORD && mult < 3)
                                    mult = 3;
                                if (tp == CellType.DOUBLE_WORD && mult < 2)
                                    mult = 2;
                            }
                            wordScore += letterScore;
                        }
                        wordScore *= mult;
                        outWords.add(str);
                        outScores.add(wordScore);
                        ret += wordScore;
                    }
                } else
                    break;
            }
        }

        for (Coordinates cd : currentCoordinates) {
            String str = "";

            int startY = cd.getY();
            boolean isCont = false;

            for (int i = cd.getY() - 1; i >= 0; i--) {
                if (isInCoordinates(currentCoordinates, i, cd.getX())) {
                    isCont = true;
                    break;
                }
                char ch = chars[i][cd.getX()];
                if (ch == 0)
                    break;
                startY = i;
                str = ch + str;
            }
            if (isCont)
                continue;
            // look for word top to bottom
            for (int i = cd.getY(); i < height; i++) {
                char ch = chars[i][cd.getX()];

                if (ch != 0) {
                    str += ch;
                    if (str.length() > 1 && dict.isWord(str)) {
                        int wordScore = 0;
                        int mult = 1;
                        for (int k = startY; k < startY + str.length(); k++) {
                            char ch2 = chars[k][cd.getX()];
                            int letterScore = TileBag.getScore(ch2);
                            if (isInCoordinates(currentCoordinates, k, cd.getX())) {
                                CellType tp = bg.cellTypes[k][cd.getX()];
                                if (tp == CellType.DOUBLE_LETTER)
                                    letterScore *= 2;
                                else if (tp == CellType.TRIPLE_LETTER)
                                    letterScore *= 3;
                                if (tp == CellType.TRIPLE_WORD && mult < 3)
                                    mult = 3;
                                if (tp == CellType.DOUBLE_WORD && mult < 2)
                                    mult = 2;
                            }
                            wordScore += letterScore;
                        }
                        wordScore *= mult;
                        outWords.add(str);
                        outScores.add(wordScore);

                        ret += wordScore;
                    }
                } else
                    break;
            }

        }
        return ret;
    }

    public static char[][] getChars(ScrabbleCellButton[][] cells, int height, int width) {
        char[][] ret = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ret[i][j] = (char) 0;
                String text = cells[i][j].getText();
                if (text.length() != 1)
                    continue;
                ret[i][j] = text.charAt(0);
            }
        }
        return ret;
    }

}
