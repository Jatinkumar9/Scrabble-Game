package tests.view;

import game.scrabble.model.GameModel;
import game.scrabble.model.Player;
import game.scrabble.view.GameView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameViewTest {

    private GameView view;
    private GameModel model;
    
    @Before
    public void init() throws Exception {
		model = new GameModel();
		model.setPlayer1(new Player("Player 1"));
		model.setPlayer2(new Player("Player 2"));
		
        view = new GameView(model);
    }
    
    @Test
    public void testBothPlayersAdded() {
    	assertEquals("Player 1", view.getModel().getPlayer1().name);
    	assertEquals("Player 2", view.getModel().getPlayer2().name);
    }
    
    /**
     * As we have not initialized GUI, board should be null
     */
    @Test
    public void testBoardNotInitialized() {
    	assertEquals(null, view.boardController);
    }
}
