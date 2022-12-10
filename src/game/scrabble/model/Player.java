package game.scrabble.model;

import game.scrabble.utils.Constants;

public class Player {
    public String name;
    static int instanceCount = 0;
    private int id;
    private int moves;
    private int points;
    public Tile [] tiles;

    public Player(String name){
        this.name = name;
        instanceCount++;
        this.id = instanceCount;
        this.points = 0;
        this.moves = 0;
        this.tiles = new Tile[Constants.TILESCOUNT];
    }

    public void setScore(int points){
        this.points = this.points + points;
    }

    public int getScore() {
        return this.points;
    }

    public void setMove(){
        this.moves++;
    }

    public int getMoves(){
        return this.moves;
    }

    public int get_id() {
        return this.id;
    }
    
    public Player makeCopy() {
        Player ret = new Player(this.name);
        ret.instanceCount = this.instanceCount;
        ret.id = this.id;
        ret.moves = this.moves;
        ret.points = this.points;
        ret.tiles = new Tile[this.tiles.length];
        for(int i=0;i<ret.tiles.length;i++)
            if(this.tiles[i]!=null)
                ret.tiles[i] = this.tiles[i].makeCopy();
        return ret;
    }
}
