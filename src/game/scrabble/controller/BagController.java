package game.scrabble.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import game.scrabble.model.Coordinates;
import game.scrabble.model.Player;
import game.scrabble.model.Tile;
import game.scrabble.utils.Algo;
import game.scrabble.utils.BoardGenerator;
import game.scrabble.utils.BoardManager;
import game.scrabble.utils.Util;
import game.scrabble.view.GameView;

public class BagController implements ActionListener {

    private GameView frame;

    ArrayList<Integer> selectedIndices = new ArrayList<Integer>();
    
    List<BoardManager> prevStates = new ArrayList<BoardManager>();
    public int currentStateIndex;

    public BagController(GameView frame) {
        this.frame = frame;
         frame.boardManager.buildBoard(BoardGenerator.SIZE, BoardGenerator.SIZE);
    }

    public void pass() {
        System.out.println("Now in pass");
        if (frame.boardManager.currentTurn == 1) {
            for (Component c : frame.bagOfPlayer1.getComponents()) {
                c.setEnabled(false);
            }
            for (Component c : frame.bagOfPlayer2.getComponents()) {
                c.setEnabled(true);
            }
            System.out.println("Passed to player 2");
            frame.boardManager.currentTurn++;
        } else {
            for (Component c : frame.bagOfPlayer2.getComponents()) {
                c.setEnabled(false);
            }
            for (Component c : frame.bagOfPlayer1.getComponents()) {
                c.setEnabled(true);
            }
            System.out.println("Passed to player 1");
            frame.boardManager.currentTurn--;
        }
        // alphabetStack.clear();
        selectedIndices.clear();
        frame.boardController.currentCoordinates.clear();
        System.out.println("Stack Cleared");
        System.out.println(frame.boardManager.currentTurn + " turn");
        int diff = prevStates.size() - 1 - this.currentStateIndex;
        for(int i=0;i<diff;i++)
            prevStates.remove(prevStates.size()-1);
        prevStates.add(frame.boardManager.getCopy(frame.boardManager.getModel()));
        this.currentStateIndex = prevStates.size()-1;
        
        if(prevStates.size()>20)
            prevStates.remove(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        int player = Integer.parseInt(command.split(",")[0]);
        if (player != this.frame.boardManager.currentTurn)
            return;
        String commandName = e.getActionCommand().split(",")[1];
        if (commandName.equals("ai")) {
            List<Coordinates> resultCoord = new ArrayList<Coordinates>();
            List<Integer> resultIndices = new ArrayList<Integer>();
            List<String> resultWords = new ArrayList<String>();
            List<Integer> resultWordScores = new ArrayList<Integer>();
            Algo algo = new Algo();
            int algoScore = algo.findNextMove(frame.boardManager, frame.boardGenerator, resultCoord, resultIndices, resultWords,
                resultWordScores);
            System.out.println(
                "RESULT: Algo Score: " + algoScore + ", indices size=" + resultIndices.size()+ ", coordinates size=" + resultCoord.size());
            if (algoScore > 0) {
                selectedIndices.clear();
                for (int i = 0; i < resultIndices.size(); i++) {
                    int ind = resultIndices.get(i);
                    Tile tile = this.frame.boardManager.currentPlayer().tiles[ind];
                    frame.currentSelectedCharacter = tile.getText();
                    selectedIndices.add(ind);
                    this.frame.boardController.putTileInLocation(resultCoord.get(i));

                }
                submit();
            } else {
                JOptionPane.showMessageDialog(frame,
                    "No valid solution found using Algo.", "No Solution.",
                    JOptionPane.INFORMATION_MESSAGE);
            }

            return;
        }
        if (commandName.equals("pass")) {
            pass();
            return;
        }
        if (e.getActionCommand().split(",")[1].equals("clear")) {
            clear();
            return;
        }
        if (e.getActionCommand().split(",")[1].equals("submit")) {
            submit();
            return;
        }
        int i = Integer.parseInt(e.getActionCommand().split(",")[1]);
        // frame.currentSelectedCharacter = e.getActionCommand().split(",")[2];
        Tile tile = this.frame.boardManager.currentPlayer().tiles[i];
        frame.currentSelectedCharacter = tile.getText();
        selectedIndices.add(i);
        if (player == 1) {
            frame.alphabetButtonsPlayer1[i].setEnabled(false);
        }
        if (player == 2) {
            frame.alphabetButtonsPlayer2[i].setEnabled(false);
        }

    }

    /**
     * updateSurroundingNodes is responsible for enabling nodes surrounding a letter
     * after a successful try.
     */
    private void updateSurroundingNodes() {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (frame.boardController.frame.cells[x][y].getText().length() != 1
                    && (!(x == 7 && y == 7)))
                    Util.displayEnabledButton(frame.boardGenerator, frame.boardController.frame.cells[x][y], false);
            }
        }
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (frame.boardController.frame.cells[x][y].getText().length() == 1) {
                    if (x>0 && frame.boardController.frame.cells[x - 1][y].getText().length() != 1) {
                        Util.displayEnabledButton(frame.boardGenerator, frame.boardController.frame.cells[x - 1][y], true);
                    }
                    if (x<14 && frame.boardController.frame.cells[x + 1][y].getText().length() != 1) {
                        Util.displayEnabledButton(frame.boardGenerator,frame.boardController.frame.cells[x + 1][y], true);
                    }
                    if (y>0 && frame.boardController.frame.cells[x][y - 1].getText().length() != 1) {
                        Util.displayEnabledButton(frame.boardGenerator,frame.boardController.frame.cells[x][y - 1], true);
                    }
                    if (y<14 && frame.boardController.frame.cells[x][y + 1].getText().length() != 1) {
                        Util.displayEnabledButton(frame.boardGenerator,frame.boardController.frame.cells[x][y + 1], true);
                    }
                }
            }
        }
    }

    private void submit() {
        if (frame.boardController.currentCoordinates.size() != this.selectedIndices.size()) {
            JOptionPane.showMessageDialog(frame,
                "Cannot submit until all selected tiles are put in board.", "Invalid Move",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (frame.boardController.currentCoordinates.size() == 0) {
            return;
        }

        boolean stillValid = Util.isBoardStillValid(frame.cells, this.frame.boardManager.dictionaryManager,
            this.frame.boardManager.boardHeight, this.frame.boardManager.boardWidth);
        if (!stillValid) {
            JOptionPane.showMessageDialog(frame,
                "The board is no more containing valid words. Resettings.", "Invalid Move",
                JOptionPane.INFORMATION_MESSAGE);
            this.clear();
            return;
        }

        for (int i = 0; i < frame.boardController.currentCoordinates.size(); i++) {
            Coordinates cd = frame.boardController.currentCoordinates.get(i);
            Tile tile = frame.boardManager.currentPlayer().tiles[this.selectedIndices.get(i)];
            frame.boardManager.tiles[cd.getY()][cd.getX()] = tile;
        }

        for (int i : this.selectedIndices) {
            this.frame.boardManager.players.get(this.frame.boardManager.currentTurn - 1).tiles[i] = null;
        }
        String result = this.frame.boardManager.updateAllPlayerTiles();
        if(result!=null && result.equals("over")) {
            JOptionPane.showMessageDialog(frame,
                "Game Over.", "Invalid Move",
                JOptionPane.INFORMATION_MESSAGE);
            this.frame.boardManager.gameOver = true;
            return;
        }
        for (int i = 0; i < 2; i++) {
            Tile[] tiles = this.frame.boardManager.players.get(i).tiles;
            JButton[] buttons = (i == 0) ? frame.alphabetButtonsPlayer1
                : frame.alphabetButtonsPlayer2;

            for (int j = 0; j < tiles.length; j++) {
                buttons[j].setText(tiles[j].getText());
            }
        }

        List<String> words = new ArrayList<String>();
        List<Integer> scrs = new ArrayList<Integer>();

        int score = Util.calculateScore(frame.boardGenerator, frame.cells, frame.boardController.currentCoordinates,
            this.frame.boardManager.dictionaryManager, words, scrs, frame.boardManager.boardHeight, frame.boardManager.boardWidth);
        Player currP = this.frame.boardManager.players.get(frame.boardManager.currentTurn - 1);
        currP.setScore(currP.getScore() + score);
        String scoreStr = "Added score: " + score + " using words:\n";
        for (int i = 0; i < words.size(); i++) {
            if (i > 0)
                scoreStr += ", ";
            scoreStr += words.get(i) + "(" + scrs.get(i) + ")";
        }
        JOptionPane.showMessageDialog(frame, scoreStr, "Score updated",
            JOptionPane.INFORMATION_MESSAGE);

        updateSurroundingNodes();
        this.frame.updateScores();
        pass();
    }

    public void clear() {
        for (int ind : selectedIndices) {
            this.frame.alphabetButtonsPlayer1[ind].setEnabled(true);
            
        }
        
        updateSurroundingNodes();
        for (Coordinates cd : this.frame.boardController.currentCoordinates) {
            frame.cells[cd.getY()][cd.getX()].setBackground(Util.getCellColor(frame.boardGenerator,  cd.getY(), cd.getX()));
        }
        frame.boardController.clearStack();
                
        this.selectedIndices.clear();
        frame.boardController.enableAllTiles();
    }
}
