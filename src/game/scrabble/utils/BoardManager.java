package game.scrabble.utils;

import java.util.ArrayList;
import java.util.List;

import game.scrabble.model.Coordinates;
import game.scrabble.model.GameModel;
import game.scrabble.model.Player;
import game.scrabble.model.Tile;
import game.scrabble.model.TileBag;

public class BoardManager {

    public Tile[][] tiles;
    public int boardWidth;
    public int boardHeight;
    public TileBag tileBag;
    public ArrayList<Player> players = new ArrayList<>();
    public int currentTurn = 1;

    public List<Coordinates> blanks = new ArrayList<>();

    public DictionaryManager dictionaryManager;
    
    public boolean gameOver = false;
    
    private GameModel model;

    public BoardManager(GameModel gameModel) {
    	this.setModel(gameModel);
    	players.add(gameModel.getPlayer1());
    	players.add(gameModel.getPlayer2());
    }

    public void buildBoard(int width, int height) {
        this.boardHeight = height;
        this.boardWidth = width;
        this.tiles = new Tile[this.boardWidth][this.boardHeight];
        dictionaryManager = new DictionaryManager(new DictionaryUtil(false));
        tileBag = new TileBag();
        tileBag.initialize();
    }

    public String updateAllPlayerTiles() {
        String a = updatePlayerTiles(0);
        String b = updatePlayerTiles(1);
        
        String ret = a+b;
        if(ret.length()==0)
            return null;
        return ret;
    }

    public String updatePlayerTiles(int index) {
        
        Player p = players.get(index);
        for (int i = 0; i < p.tiles.length; i++) {
            if(tileBag.tiles.size()==0) {
                return "over";
            }
            if (p.tiles[i] == null) {
                p.tiles[i] = tileBag.removeAndGiveARandomTile();
            }
        }
        return "";
    }

    public Player currentPlayer() {
        return this.players.get(this.currentTurn - 1);
    }

    public BoardManager getCopy(GameModel gameModel) {
        BoardManager ret = new BoardManager(gameModel);
        ret.boardHeight = this.boardHeight;
        ret.boardWidth = this.boardWidth;
        ret.currentTurn = this.currentTurn;
        ret.tiles = new Tile[ret.boardHeight][ret.boardWidth];
        for (int i = 0; i < ret.boardHeight; i++) {
            for (int j = 0; j < ret.boardWidth; j++) {
                if (this.tiles[i][j] != null)
                    ret.tiles[i][j] = this.tiles[i][j].makeCopy();
            }
        }
        ret.players = new ArrayList<Player>();
        for (Player p : this.players)
            ret.players.add(p.makeCopy());

        ret.blanks = new ArrayList<Coordinates>();
        for (Coordinates cd : this.blanks)
            ret.blanks.add(new Coordinates(cd.getY(), cd.getX()));
        ret.dictionaryManager = this.dictionaryManager;
        ret.tileBag = this.tileBag.makeCopy();
        return ret;
    }

    public void loadFromCopy(BoardManager b) {
        this.boardHeight = b.boardHeight;
        this.boardWidth = b.boardWidth;
        this.currentTurn = b.currentTurn;
        this.tiles = new Tile[this.boardHeight][this.boardWidth];

        for (int i = 0; i < this.boardHeight; i++) {
            for (int j = 0; j < this.boardWidth; j++) {
                if (b.tiles[i][j] != null)
                    this.tiles[i][j] = b.tiles[i][j].makeCopy();
            }
        }
        this.players = new ArrayList<Player>();
        for (Player p : b.players)
            this.players.add(p.makeCopy());

        this.blanks = new ArrayList<Coordinates>();
        for (Coordinates cd : b.blanks)
            this.blanks.add(new Coordinates(cd.getY(), cd.getX()));
        this.dictionaryManager = b.dictionaryManager;
        this.tileBag = b.tileBag.makeCopy();
    }

	public GameModel getModel() {
		return model;
	}

	public void setModel(GameModel model) {
		this.model = model;
	}
}
