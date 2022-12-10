package game.scrabble.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileBag {
    public List<Tile> tiles;

    private static Random rand = new Random();
    

    public TileBag() {
        this.tiles = new ArrayList<Tile>();
    }

    public void initialize() {
        for (int i = 'A'; i <= 'Z'; i++) {
            char ch = (char) i;
            int count = getCount(ch);
            for (int j = 0; j < count; j++)
                tiles.add(new Tile(ch));
        }
        for(int i=0;i<getCount(' ');i++) {
            tiles.add(new Tile(' '));
        }
    }
    
    private int getIndexOfDebug(char ch) {
        for(int i=0;i<this.tiles.size();i++) {
            if(this.tiles.get(i).character== ch)
                return i;
        }
        return -1;
    }
    
    public TileBag makeCopy() {
        TileBag ret = new TileBag();
        ret.tiles = new ArrayList<Tile>();
        for(Tile t : tiles)
            ret.tiles.add(t.makeCopy());
        return ret;
    }
    
    public Tile removeAndGiveARandomTile() {
        int index =rand.nextInt(tiles.size()); 
        char [] set = new char [] {'M', 'A', 'N', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'O', 'P', 'Q', 'R', 
            'S', 'T', 'U', 'V', 'W', 'X', 'Y','Z' };
        //if(tiles.size()>100-set.length) {
        //    index = getIndexOfDebug(set[100-tiles.size()]);
        //}
        
        //if(tiles.size()==98)
        //    index = getIndexOfDebug('M');
        //if(tiles.size()==97)
        //    index = getIndexOfDebug('A');
        //if(tiles.size()==96)
        //    index = getIndexOfDebug('N');
        Tile tile = tiles.get(index);
        System.out.println("Given tile: " + tile.getText());
        tiles.remove(index);
        return tile;
    }


    public static int getCount(char ch) {
        switch (ch) {
        case 'K':
        case 'J':
        case 'X':
        case 'Q':
        case 'Z':
            return 1;

        case 'F':
        case 'H':
        case 'V':
        case 'W':
        case 'Y':
        case 'C':
        case 'M':
        case 'P':
        case 'B':
            return 2;

        case 'G':
            return 3;

        case 'D':
        case 'L':
        case 'S':
        case 'U':
            return 4;

        case 'N':
        case 'R':
        case 'T':
            return 6;

        case 'O':
            return 8;

        case 'A':
        case 'I':
            return 9;

        case 'E':
            return 12;

        case ' ':
            return 0;

        default:
            return 0;

        }
    }

    public static int getScore(char ch) {
        switch (ch) {
        case 'E':
        case 'A':
        case 'I':
        case 'O':
        case 'N':
        case 'R':
        case 'T':
        case 'L':
        case 'S':
        case 'U':
            return 1;

        case 'D':
        case 'G':
            return 2;

        case 'B':
        case 'C':
        case 'M':
        case 'P':
            return 3;

        case 'F':
        case 'H':
        case 'V':
        case 'W':
        case 'Y':
            return 4;

        case 'K':
            return 5;

        case 'J':
        case 'X':
            return 8;

        case 'Q':
        case 'Z':
            return 10;

        default:
            return 0;

        }
    }

}
