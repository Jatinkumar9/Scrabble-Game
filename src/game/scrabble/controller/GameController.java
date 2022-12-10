package game.scrabble.controller;

import game.scrabble.model.GameModel;
import game.scrabble.model.Player;
import game.scrabble.view.GameView;

public class GameController {

    public static void main(String[] args) {
    	//initialize model
    	GameModel gameModel = new GameModel();
    	gameModel.setPlayer1(new Player("Player 1"));
    	gameModel.setPlayer2(new Player("Player 2"));

    	//initialize view
        GameView gameView = new GameView(gameModel);
        gameView.initGame();
    }

}
