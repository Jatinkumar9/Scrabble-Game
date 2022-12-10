package game.scrabble.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import game.scrabble.model.Coordinates;
import game.scrabble.model.Player;
import game.scrabble.model.Tile;
import game.scrabble.model.TileBag;

public class BoardSerializationUtil {

    private static String BOARDWIDTHSTR = "BOARDWIDTH";
    private static String BOARDHEIGHTSTR = "BOARDHEIGHT";
    private static String TILEBAGSTR = "TILEBAG";
    private static String CURRENTTURNSTR = "CURRENTTURN";
    private static String BLANKSSTR = "BLANKS";
    private static String BOARDSTR = "BOARD";
    private static String PLAYERSTR = "PLAYER";
    private static String NAMESTR = "NAME";
    private static String POINTSSTR = "POINTS";
    private static String TILESSTR = "TILES";

    private static char EQUAL = '=';
    private static char COMMA = ',';
    private static char SEMICOLON = ';';
    private static char COLON = ':';
    private static char NEWLINE = '\n';
    private static String NOTILE = "_";

    public void writeBoard(BoardManager b) {
        StringBuilder sb = new StringBuilder();
        sb.append(BOARDWIDTHSTR + EQUAL + b.boardWidth + NEWLINE);
        sb.append(BOARDHEIGHTSTR + EQUAL + b.boardHeight + NEWLINE);
        sb.append(CURRENTTURNSTR + EQUAL + b.currentTurn + NEWLINE);

        sb.append(TILEBAGSTR + EQUAL);
        for (int i = 0; i < b.tileBag.tiles.size(); i++) {
            Tile tile = b.tileBag.tiles.get(i);
            if (i > 0)
                sb.append(COMMA);
            sb.append(tile.getText());
        }
        sb.append(SEMICOLON + NEWLINE);

        sb.append(BLANKSSTR + EQUAL);
        for (int i = 0; i < b.blanks.size(); i++) {
            if (i > 0)
                sb.append(SEMICOLON);
            Coordinates bk = b.blanks.get(i);
            sb.append(bk.getY() + "," + bk.getX());
        }
        sb.append(NEWLINE);

        sb.append(BOARDSTR + EQUAL);
        for (int i = 0; i < b.boardHeight; i++) {
            if (i > 0)
                sb.append(SEMICOLON);
            for (int j = 0; j < b.boardWidth; j++) {
                if (j > 0)
                    sb.append(COMMA);
                Tile tile = b.tiles[i][j];
                if(tile!=null)
                    sb.append(tile.getText());
                else 
                    sb.append(NOTILE);
            }
        }
        sb.append(NEWLINE);
        for (Player p : b.players) {
            sb.append(PLAYERSTR + EQUAL + NAMESTR + COLON + p.name + SEMICOLON + POINTSSTR + COLON
                + p.getScore() + SEMICOLON + TILESSTR + COLON);
            for (int i = 0; i < p.tiles.length; i++) {
                if (i > 0)
                    sb.append(COMMA);
                sb.append(p.tiles[i].getText());
            }
            sb.append(SEMICOLON);
            sb.append(NEWLINE);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.BOARD_STATE_FILE));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadBoard(BoardManager b) {
        
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(Constants.BOARD_STATE_FILE);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() > 0)
                    lines.add(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        if(lines.size()==0)
            return false;
        b.players.clear();
        
        for(String line : lines) {
            int index = line.indexOf(EQUAL);
            System.out.println("Debug:" + line);
            String aa = line.substring(0,index).trim();
            String bb = line.substring(index+1).trim();
            if(aa.equals(BOARDWIDTHSTR)) {
                b.boardWidth = Integer.parseInt(bb.trim());
            } else if(aa.equals(BOARDHEIGHTSTR)) {
                b.boardHeight = Integer.parseInt(bb.trim());
            } else if(aa.equals(CURRENTTURNSTR)) {
                b.currentTurn = Integer.parseInt(bb.trim());
            } else if (aa.equals(TILEBAGSTR)) {
                b.tileBag = new TileBag();
                for(int i=0;i<bb.length();i+=2) {
                    Tile tt = new Tile(bb.charAt(i));
                    b.tileBag.tiles.add(tt);
                } 
            } else if(aa.equals(BLANKSSTR)) {
                String [] sp1 = bb.split(";");
                for(int i=0;i<sp1.length;i++) {
                    String ss1 = sp1[i].trim();
                    if(ss1.length()==0)
                        continue;
                    String [] sp2 =ss1.split(",");
                    b.blanks.add(new Coordinates(Integer.parseInt(sp2[0]), Integer.parseInt(sp2[1])));
                } 
            } else if(aa.equals(BOARDSTR)) {
                String [] sp1 = bb.split(";");
                List<String> sp1s = new ArrayList<String>();
                for(int i=0;i<sp1.length;i++) {
                    String sstemp = sp1[i].trim();
                    if(sstemp.length()>0)
                        sp1s.add(sstemp);
                }
                for(int i=0;i<sp1s.size();i++) {
                    String ss = sp1s.get(i);
                    for(int j=0;j<ss.length();j+=2) {
                         if(ss.charAt(j)==NOTILE.charAt(0))
                             continue;
                         Tile tt = new Tile(ss.charAt(j));
                         b.tiles[i][j/2] = tt;
                    }
                }
            } else if(aa.equals(PLAYERSTR)) {
                String [] sp1 = bb.split(";");
                List<String> sp1s = new ArrayList<String>();
                for(int i=0;i<sp1.length;i++) {
                    String sstemp = sp1[i].trim();
                    if(sstemp.length()>0)
                        sp1s.add(sstemp);
                }
                String name="";
                int points = 0;
                List<Tile> tiles = new ArrayList<Tile>();
                for(int i=0;i<sp1s.size();i++) {
                    String ss = sp1s.get(i);
                    int ind2 = ss.indexOf(COLON);
                    if(ind2<=0)
                        continue;
                    String aa1 = ss.substring(0,ind2);
                    String bb1 = ss.substring(ind2+1);
                   
                    if(aa1.equals(NAMESTR)) {
                        name = bb1;
                    } else if(aa1.equals(POINTSSTR)) {
                        points = Integer.parseInt(bb1);
                    } else if(aa1.equals(TILESSTR)) {
                        for(int j=0;j<bb1.length();j+=2) {
                            Tile tt = new Tile(bb1.charAt(j));
                            tiles.add(tt);
                       }
                    }
                }
                Player p = new Player(name);
                p.setScore(points);
                p.tiles = (Tile[]) tiles.toArray(new Tile[tiles.size()]);
                b.players.add(p);
            }
        }
        return true;
    }
}
