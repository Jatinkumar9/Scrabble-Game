package game.scrabble.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import game.scrabble.controller.BagController;
import game.scrabble.controller.BoardController;
import game.scrabble.controller.MainButtonsController;
import game.scrabble.model.GameModel;
import game.scrabble.model.ScrabbleCellButton;
import game.scrabble.model.Tile;
import game.scrabble.utils.BoardGenerator;
import game.scrabble.utils.BoardManager;
import game.scrabble.utils.Util;

public class GameView extends JFrame {
    private static final long serialVersionUID = 1L;

	public BagController bagController;

    JButton submitButtonPlayer1, submitButtonPlayer2;
    public ScrabbleCellButton[][] cells = new ScrabbleCellButton[15][15];

    private JButton undoButton,redoButton, saveButton, loadButton;
    private JButton replacePlayer1btn, replacePlayer2btn, aiPlayer1btn, aiPlayer2btn;
    private JButton passPlayer1btn, passPlayer2btn, clearPlayer1, clearPlayer2;
    private GameModel model;
    public BoardController boardController;
    MainButtonsController mainButtonsController;

    public BoardManager boardManager;
    JPanel mainButtonsPanel1;
    JPanel mainButtonsPanel2;

	public JPanel bagOfPlayer1;

    public JPanel bagOfPlayer2;

    JPanel boardPanel;
    private JLabel scoreTextLabel1, scoreTextLabel2;
    private JLabel player1score, player2score;
    public JButton[] alphabetButtonsPlayer1, alphabetButtonsPlayer2;
    
    public BoardGenerator boardGenerator;


    public String currentSelectedCharacter;
    
    public GameView(GameModel gameModel) {
    	this.model = gameModel;
    }
    
    public void initGame() {
        UIManager.put("Button.disabledText", new ColorUIResource(Color.DARK_GRAY));
        this.boardGenerator = new BoardGenerator();
        this.boardManager = new BoardManager(this.model);

        this.boardGenerator.init(this.boardGenerator.cellTypes);
        boardController = new BoardController(this);
        bagController = new BagController(this);
        mainButtonsController = new MainButtonsController(this);
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gb);
        
        mainButtonsPanel1 = new JPanel(new GridLayout(3, 3, 10, 10));
        mainButtonsPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainButtonsPanel1.setBackground(Color.pink);

        mainButtonsPanel2 = new JPanel(new GridLayout(3, 3, 10, 10));
        mainButtonsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainButtonsPanel2.setBackground(Color.pink);

        bagOfPlayer1 = new JPanel(new GridLayout(16, 3, 10, 10));
        bagOfPlayer1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        boardPanel = new JPanel(new GridLayout(16, 16, 3, 3));
        boardPanel.setBorder(BorderFactory.createEtchedBorder());
        bagOfPlayer2 = new JPanel(new GridLayout(16, 3, 10, 10));
        bagOfPlayer2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel player1 = new JLabel("Player 1", SwingConstants.CENTER);
        JLabel player2 = new JLabel("Player 2", SwingConstants.CENTER);
        player1.setBorder(BorderFactory.createEtchedBorder());
        player2.setBorder(BorderFactory.createEtchedBorder());
        bagOfPlayer1.add(player1);
        bagOfPlayer2.add(player2);

        alphabetButtonsPlayer1 = new JButton[7];
        alphabetButtonsPlayer2 = new JButton[7];

        scoreTextLabel1 = new JLabel("SCORE", SwingConstants.CENTER);
        bagOfPlayer1.add(scoreTextLabel1);
        scoreTextLabel2 = new JLabel("SCORE", SwingConstants.CENTER);
        bagOfPlayer2.add(scoreTextLabel2);

        player1score = new JLabel("0", SwingConstants.CENTER);
        player2score = new JLabel("0", SwingConstants.CENTER);
        bagOfPlayer1.add(player1score);
        bagOfPlayer2.add(player2score);

        char a = 'a';

        for (int i = 0; i < 16; i++) {
            boardPanel.add(new JLabel("    " + (char) (a + i - 1) + "    ", SwingConstants.CENTER));
        }

        for (int i = 0; i < 15; i++) {
            boardPanel.add(new JLabel((i+1) + "", SwingConstants.CENTER));
            for (int j = 0; j < 15; j++) {
                cells[i][j] = new ScrabbleCellButton("", i, j);
                cells[i][j].setActionCommand(i + " " + j);
                cells[i][j].addActionListener(boardController);
                cells[i][j].setBackground(Util.getCellColor(this.boardGenerator,  i, j));
                cells[i][j].setText(Util.getCellTypeString(this.boardGenerator.cellTypes[i][j]));
                Util.displayEnabledButton(this.boardGenerator, cells[i][j], (i == 7 && j == 7));

                boardPanel.add(cells[i][j], CENTER_ALIGNMENT);
            }
        }
        String player1Alphabet, player2Alphabet;
        boardManager.updateAllPlayerTiles();
        for (int i = 0; i < 7; i++) {
            Tile p1Tile = boardManager.players.get(0).tiles[i];
            Tile p2Tile = boardManager.players.get(1).tiles[i];

            player1Alphabet = "" + p1Tile.getText();
            player2Alphabet = "" + p2Tile.getText();

            alphabetButtonsPlayer1[i] = new JButton(player1Alphabet);
            bagOfPlayer1.add(alphabetButtonsPlayer1[i]);
            alphabetButtonsPlayer1[i].addActionListener(bagController);
            alphabetButtonsPlayer1[i].setActionCommand(1 + "," + i + "," + player1Alphabet);

            alphabetButtonsPlayer2[i] = new JButton(player2Alphabet);
            bagOfPlayer2.add(alphabetButtonsPlayer2[i]);
            alphabetButtonsPlayer2[i].addActionListener(bagController);
            alphabetButtonsPlayer2[i].setActionCommand(2 + "," + i + "," + player2Alphabet);
        }
        
        undoButton = new JButton("Undo");
        undoButton.setActionCommand("undo");
        undoButton.addActionListener(mainButtonsController);
        mainButtonsPanel1.add(undoButton);
        
        redoButton = new JButton("Redo");
        redoButton.setActionCommand("Redo");
        redoButton.addActionListener(mainButtonsController);
        mainButtonsPanel1.add(redoButton);
        

        saveButton = new JButton("Save Game");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(mainButtonsController);
        mainButtonsPanel2.add(saveButton);
        
        loadButton = new JButton("Load Game");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(mainButtonsController);
        mainButtonsPanel2.add(loadButton);

        
        
        replacePlayer1btn = new JButton("Replace");
        replacePlayer2btn = new JButton("Replace");
        aiPlayer1btn = new JButton("AI Play");
        aiPlayer2btn = new JButton("AI Play");
        
        passPlayer1btn = new JButton("Pass");
        passPlayer2btn = new JButton("Pass");
        clearPlayer1 = new JButton("Clear");
        clearPlayer2 = new JButton("Clear");
        submitButtonPlayer1 = new JButton("SUBMIT");
        submitButtonPlayer2 = new JButton("SUBMIT");
        
        bagOfPlayer1.add(replacePlayer1btn);
        bagOfPlayer1.add(aiPlayer1btn);
        bagOfPlayer1.add(passPlayer1btn);
        bagOfPlayer1.add(clearPlayer1);

        bagOfPlayer2.add(replacePlayer2btn);
        bagOfPlayer2.add(aiPlayer2btn);
        bagOfPlayer2.add(passPlayer2btn);
        bagOfPlayer2.add(clearPlayer2);
        bagOfPlayer1.add(submitButtonPlayer1);
        bagOfPlayer2.add(submitButtonPlayer2);
        
        submitButtonPlayer2.addActionListener(bagController);
        submitButtonPlayer1.addActionListener(bagController);
        
        replacePlayer1btn.addActionListener(bagController);
        aiPlayer1btn.addActionListener(bagController);
        passPlayer1btn.addActionListener(bagController);
        clearPlayer1.addActionListener(bagController);
        
        replacePlayer2btn.addActionListener(bagController);
        aiPlayer2btn.addActionListener(bagController);
        passPlayer2btn.addActionListener(bagController);
        clearPlayer2.addActionListener(bagController);
        
        submitButtonPlayer1.addActionListener(bagController);
        submitButtonPlayer2.addActionListener(bagController);
        replacePlayer1btn.setActionCommand(1 + "," + "replace");
        aiPlayer1btn.setActionCommand(1 + "," + "ai");
        
        passPlayer1btn.setActionCommand(1 + "," + "pass");
        clearPlayer1.setActionCommand(1 + "," + "clear");
        
        replacePlayer2btn.setActionCommand(2 + "," + "replace");
        aiPlayer2btn.setActionCommand(2 + "," + "ai");

        passPlayer2btn.setActionCommand(2 + "," + "pass");
        clearPlayer2.setActionCommand(2 + "," + "clear");
        
        submitButtonPlayer1.setActionCommand(1 + "," + "submit");
        submitButtonPlayer2.setActionCommand(2 + "," + "submit");

        JLabel label = new JLabel("S C R A B B L E");
        label.setFont(new Font("Copperplate Gothic Bold", Font.ROMAN_BASELINE, 50));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        add(mainButtonsPanel1, c);

        
        c.ipadx = 10;
        c.ipady = 10;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        add(label, c);
        
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        add(mainButtonsPanel2, c);
        

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(2, 5, 2, 5);
        add(bagOfPlayer1, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.gridheight = 2;
        add(boardPanel, c);

        c.gridx = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        add(bagOfPlayer2, c);

        setSize(new Dimension(975, 830));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        if (this.boardManager.currentTurn == 2) {
            for (Component cc : this.bagOfPlayer1.getComponents()) {
                cc.setEnabled(false);
            }
            for (Component cc : this.bagOfPlayer2.getComponents()) {
                cc.setEnabled(true);
            }
            
        } else {
            for (Component cc : this.bagOfPlayer2.getComponents()) {
                cc.setEnabled(false);
            }
            for (Component cc : this.bagOfPlayer1.getComponents()) {
                cc.setEnabled(true);
            }
            
        }
    }

    public void updateScores() {
        this.player1score.setText(""+ this.boardManager.players.get(0).getScore());
        this.player2score.setText(""+ this.boardManager.players.get(1).getScore());
    }
    
    public GameModel getModel() {
		return model;
	}

	public void setModel(GameModel model) {
		this.model = model;
	}
}
