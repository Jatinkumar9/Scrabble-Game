package game.scrabble.model;

public class Tile {
    public char character;
    public char charIfBlank;
    
    public Tile(char character) {
        this.character = character;
        this.charIfBlank = ' ';
    }
    
    public String getText() {
        if(this.character!=' ')
            return this.character+"";
        else 
            return this.charIfBlank+"";
    }
    
    public boolean isBlank() {
        return this.character == ' ';
    }
    
    public Tile makeCopy() {
        Tile ret = new Tile(this.character);
        ret.charIfBlank = this.charIfBlank;
        return ret;
    }
}
