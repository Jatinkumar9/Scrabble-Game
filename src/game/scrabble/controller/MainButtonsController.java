package game.scrabble.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import game.scrabble.utils.BoardManager;
import game.scrabble.utils.BoardSerializationUtil;
import game.scrabble.view.GameView;

public class MainButtonsController implements ActionListener {

    private GameView frame;

    public MainButtonsController(GameView boardFrame) {
        this.frame = boardFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String command = e.getActionCommand();
        BoardSerializationUtil bsu = new BoardSerializationUtil();

        if (command.equalsIgnoreCase("save")) {
            bsu.writeBoard(frame.boardManager);
            JOptionPane.showMessageDialog(frame, "Current state of board saved to file.", "Save",
                JOptionPane.INFORMATION_MESSAGE);
        } else if (command.equalsIgnoreCase("load")) {
            bsu.loadBoard(frame.boardManager);

            updateUIUsingBoardManager();

            JOptionPane.showMessageDialog(frame, "Board loaded from file.", "Load",
                JOptionPane.INFORMATION_MESSAGE);
        } else if (command.equalsIgnoreCase("undo")) {
            if (frame.bagController.currentStateIndex > 0) {
                frame.bagController.currentStateIndex--;
                frame.boardManager = frame.bagController.prevStates
                    .get(frame.bagController.currentStateIndex);
                updateUIUsingBoardManager();
            } else {
                JOptionPane.showMessageDialog(frame, "No more undos left.", "Undo",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (command.equalsIgnoreCase("redo")) {
            if (frame.bagController.currentStateIndex < (frame.bagController.prevStates.size()-1)) {
                frame.bagController.currentStateIndex++;
                frame.boardManager = frame.bagController.prevStates
                    .get(frame.bagController.currentStateIndex);
                updateUIUsingBoardManager();
            } else {
                JOptionPane.showMessageDialog(frame, "No more redos left.", "Redo",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void updateUIUsingBoardManager() {
        frame.boardController.enableAllTiles(); 
        frame.boardController.currentCoordinates.clear();
        frame.bagController.selectedIndices.clear();
        BoardManager bm = frame.boardManager;
        for (int i = 0; i < frame.boardManager.boardHeight; i++) {
            for (int j = 0; j < frame.boardManager.boardWidth; j++) {
                String text = "";
                if (frame.boardManager.tiles[i][j] != null)
                    text = frame.boardManager.tiles[i][j].getText();
                if(frame.cells[i][j].getText().length()!=2 || text.length()==1)
                    frame.cells[i][j].setText(text);
            }
        }
        frame.updateScores();
        for (int i = 0; i < frame.alphabetButtonsPlayer1.length; i++) {
            frame.alphabetButtonsPlayer1[i].setText(bm.players.get(0).tiles[i].getText());
        }
        
        for (int i = 0; i < frame.alphabetButtonsPlayer2.length; i++) {
            frame.alphabetButtonsPlayer2[i].setText(bm.players.get(1).tiles[i].getText());
        }
        
        BoardManager manager = frame.boardManager;
        if (manager.currentTurn == 2) {
            for (Component c : frame.bagOfPlayer1.getComponents()) {
                c.setEnabled(false);
            }
            for (Component c : frame.bagOfPlayer2.getComponents()) {
                c.setEnabled(true);
            }
            
        } else {
            for (Component c : frame.bagOfPlayer2.getComponents()) {
                c.setEnabled(false);
            }
            for (Component c : frame.bagOfPlayer1.getComponents()) {
                c.setEnabled(true);
            }
            
        }
        
        frame.boardController.enableAllTiles();
    }

}
