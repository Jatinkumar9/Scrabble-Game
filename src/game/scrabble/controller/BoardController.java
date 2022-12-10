package game.scrabble.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import game.scrabble.model.CellType;
import game.scrabble.model.Coordinates;
import game.scrabble.utils.Util;
import game.scrabble.view.GameView;

public class BoardController implements ActionListener {
    GameView frame;

    public ArrayList<Coordinates> currentCoordinates;

    public BoardController(GameView boardFrame) {
        frame = boardFrame;
        currentCoordinates = new ArrayList<>();
    }

    public void clearStack() {
        // clear character stack
        // clear adjacent active cells for this move
        for (Coordinates c : currentCoordinates) {
            if (frame.cells[c.getY()][c.getX()].getText().length() == 1)
                frame.cells[c.getY()][c.getX()]
                    .setText(Util.getCellTypeString(frame.boardGenerator.cellTypes[c.getY()][c.getX()]));
            if (currentCoordinates.indexOf(c) == 0) {
                Util.displayEnabledButton(frame.boardGenerator, frame.cells[c.getY()][c.getX()], true);
            } else {
                System.out.println("unabling button at x:" + c.getY() + " y:" + c.getX());
                setEnableFalse(c);
            }
        }

        currentCoordinates.clear();
    }

    private void setEnableFalse(Coordinates c) {
        Util.displayEnabledButton(frame.boardGenerator, frame.cells[c.getY()][c.getX()], false);
    }

    private void setEnableTrueIfFree(Coordinates c) {
        if (c == null)
            return;

        if (currentCoordinates.contains(c)) {
            return;
        }

        if (frame.cells[c.getY()][c.getX()].getText().length() == 1)
            return;

        Util.displayEnabledButton(frame.boardGenerator,frame.cells[c.getY()][c.getX()], true);
    }

    public void enableAllTiles() {
        boolean[][] enabled = new boolean[frame.boardManager.boardHeight][frame.boardManager.boardWidth];

        for (int i = 0; i < frame.boardManager.boardHeight; i++) {
            for (int j = 0; j < frame.boardManager.boardWidth; j++) {
                if (frame.boardManager.tiles[i][j] != null) {

                    Coordinates coordinates[] = frame.cells[i][j].getAdjacentCells();
                    Coordinates top = coordinates[0];
                    Coordinates right = coordinates[1];
                    Coordinates down = coordinates[2];
                    Coordinates left = coordinates[3];

                    if (frame.boardManager.tiles[top.getY()][top.getX()] == null)
                        enabled[top.getY()][top.getX()] = true;
                    if (frame.boardManager.tiles[right.getY()][right.getX()] == null)
                        enabled[right.getY()][right.getX()] = true;
                    if (frame.boardManager.tiles[down.getY()][down.getX()] == null)
                        enabled[down.getY()][down.getX()] = true;
                    if (frame.boardManager.tiles[left.getY()][left.getX()] == null)
                        enabled[left.getY()][left.getX()] = true;
                }

            }
        }

        for (int i = 0; i < frame.boardManager.boardHeight; i++) {
            for (int j = 0; j < frame.boardManager.boardWidth; j++) {
                if (frame.boardGenerator.cellTypes[i][j] == CellType.STAR
                    && frame.boardManager.tiles[i][j].getText().length() != 1)
                    enabled[i][j] = true;
            }
        }

        for (int i = 0; i < frame.boardManager.boardHeight; i++) {
            for (int j = 0; j < frame.boardManager.boardWidth; j++) {
                Util.displayEnabledButton(frame.boardGenerator,frame.cells[i][j], enabled[i][j]);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        int i = Integer.parseInt(command.split(" ")[0]);
        int j = Integer.parseInt(command.split(" ")[1]);
        Coordinates currentCoord = new Coordinates(i, j);
        putTileInLocation(currentCoord);
    }

    public void putTileInLocation(Coordinates currentCoord) {
        int i = currentCoord.getY();
        int j = currentCoord.getX();
        if (i < 15 && j < 15) {
            if ((frame.bagController.selectedIndices.size() - this.currentCoordinates.size()) > 1) {
                JOptionPane.showMessageDialog(frame,
                    "Multiple tiles selected. Please select only one.", "Invalid Move",
                    JOptionPane.INFORMATION_MESSAGE);
                frame.bagController.clear();
                return;
            }

            if ((frame.bagController.selectedIndices.size() - this.currentCoordinates.size()) < 1) {
                JOptionPane.showMessageDialog(frame, "Tile already placed. Please selet next tile.",
                    "Invalid Move", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            frame.cells[i][j].setText(frame.currentSelectedCharacter);
            Util.setupButtonTextColor(frame.boardGenerator, frame.cells[i][j], true);
            currentCoordinates.add(currentCoord);
            Util.displayEnabledButton(frame.boardGenerator,frame.cells[i][j], false);
            Coordinates coordinates[] = frame.cells[i][j].getAdjacentCells();
            Coordinates top = coordinates[0];
            Coordinates right = coordinates[1];
            Coordinates down = coordinates[2];
            Coordinates left = coordinates[3];

            setEnableTrueIfFree(top);
            setEnableTrueIfFree(right);
            setEnableTrueIfFree(down);
            setEnableTrueIfFree(left);

        }
    }
}
