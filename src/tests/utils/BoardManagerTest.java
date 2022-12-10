package tests.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import game.scrabble.model.GameModel;
import game.scrabble.model.Player;
import game.scrabble.utils.BoardManager;

public class BoardManagerTest {
	private static GameModel model;
	
	@BeforeAll
	static void init() {
		model = new GameModel();
		model.setPlayer1(new Player("Player 1"));
		model.setPlayer2(new Player("Player 2"));
	}
	
    @Test
    void testBoardManagerTestTileBag() {
        BoardManager bm = new BoardManager(model);
        bm.buildBoard(15, 15);

        assertNotNull(bm.tileBag);
        assertEquals(bm.tileBag.tiles.size(), 98);
    }
    
    @Test
    void testBoardManagerCheckPlayersExist() {
        BoardManager bm = new BoardManager(model);
        bm.buildBoard(15, 15);

        assertNotNull(bm.players);
        assertEquals(bm.players.size(), 2);
    }
    
    
    @Test
    void testBoardManagerTestTiles() {
        BoardManager bm = new BoardManager(model);
        bm.buildBoard(15, 15);

        assertNotNull(bm.tiles);
        assertEquals(15, bm.tiles.length);
        assertEquals(15, bm.tiles[0].length);
    }
    
    @Test
    void testBoardManagerTestCopy() {
        BoardManager bm = new BoardManager(model);
        bm.buildBoard(15, 15);
        
        BoardManager bm2 = bm.getCopy(bm.getModel());

        assertEquals(bm.boardWidth, bm2.boardWidth);
        assertEquals(bm.boardHeight, bm2.boardHeight);
        
    }
    
    
}
