package game.scrabble.utils;

import game.scrabble.model.CellType;

public class BoardGenerator {
    
    public static int SIZE = 15;
    
    public CellType[][] cellTypes = new CellType[SIZE][SIZE];
    
    public void init(CellType[][] cellTypes) {
        for (int a = 0; a < SIZE; a++) {
            for (int b = 0; b < SIZE; b++) {
                if (a % 7 == 0 && b % 7 == 0) {
                    if (a == 7 && b == 7) {
                        cellTypes[a][b] = CellType.STAR;
                    } else
                        cellTypes[a][b] = CellType.TRIPLE_WORD;
                } else if (Math.abs(a - 7) == Math.abs(b - 7)) {
                    int v = Math.abs(a - 7);
                    if (v == 1)
                        cellTypes[a][b] = CellType.DOUBLE_LETTER;
                    else if (v == 2) {
                        cellTypes[a][b] = CellType.TRIPLE_LETTER;
                    } else if (v <= 6)
                        cellTypes[a][b] = CellType.DOUBLE_WORD;
                }

                else if (((a == 0 || a == 15) && (b == 3 || b == 11))
                    || ((b == 0 || b == 15) && (a == 3 || a == 11))) {
                    cellTypes[a][b] = CellType.DOUBLE_LETTER;
                } else if (((a == 5 || a == 9) && (b == 1 || b == 13))
                    || ((b == 5 || b == 9) && (a == 1 || a == 13))) {
                    cellTypes[a][b] = CellType.TRIPLE_LETTER;
                } else if (((a == 6 || a == 8) && (b == 2 || b == 12))
                    || ((b == 6 || b == 8) && (a == 2 || a == 12))) {
                    cellTypes[a][b] = CellType.DOUBLE_LETTER;
                } else if (((a == 7) && (b == 3 || b == 11)) || ((b == 7) && (a == 3 || a == 11))) {
                    cellTypes[a][b] = CellType.DOUBLE_LETTER;
                } else
                    cellTypes[a][b] = CellType.NORMAL;
            }
        }
    }

}
