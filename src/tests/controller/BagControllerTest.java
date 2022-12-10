package tests.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import game.scrabble.controller.BagController;
import game.scrabble.model.GameModel;
import game.scrabble.model.Player;
import game.scrabble.view.GameView;

public class BagControllerTest {

	private BagController bagController;
    private GameView view;
    private GameModel model;
    
	@Before
	public void init() throws Exception {
		model = new GameModel();
		model.setPlayer1(new Player("Player 1"));
		model.setPlayer2(new Player("Player 2"));
		
        view = new GameView(model);
        view.initGame();
       
		bagController = new BagController(view);
	}

	@Test
	public void testBagCurrentStateAtBeginning() {
		assertEquals(0, bagController.currentStateIndex);
	}
}
