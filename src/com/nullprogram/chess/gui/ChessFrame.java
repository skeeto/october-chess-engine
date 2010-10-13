package com.nullprogram.chess.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.BoardListener;
import com.nullprogram.chess.Player;
import com.nullprogram.chess.Game;

import com.nullprogram.chess.boards.EmptyBoard;

/**
 * The JFrame that contains all GUI elements.
 */
public class ChessFrame extends JFrame implements BoardListener {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** The board display. */
    private BoardPanel display;

    /** The progress bar on the display. */
    private StatusBar progress;

    /** Subclass instance for dealing with the menu. */
    private MenuHandler handler;

    /** The current game. */
    private Game game;

    /**
     * Create a new ChessFrame for the given board.
     */
    public ChessFrame() {
        super("Chess");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        handler = new MenuHandler(this);
        handler.setUpMenu();

        display = new BoardPanel(new EmptyBoard());
        progress = new StatusBar();
        setStatus("Ready.");
        add(display);
        add(progress);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Set up a new game.
     */
    public final void newGame() {
        NewGame ngFrame = new NewGame(this);
        ngFrame.setVisible(true);
        Game newGame = ngFrame.getGame();
        if (newGame == null) {
            return;
        }
        game = newGame;
        Board board = game.getBoard();
        board.addBoardListener(this);
        display.setBoard(board);
        display.invalidate();
        setSize(getPreferredSize());
        handler.gameMode(true);
        game.begin();
    }

    /**
     * Tells the display that the game has finished.
     */
    public final void endGame() {
        handler.gameMode(false);
    }

    /**
     * Call undo() on the game.
     */
    public final void undo() {
        game.undo();
    }

    /**
     * Return the GUI (human) play handler.
     *
     * @return the player
     */
    public final Player getPlayer() {
        return display;
    }

    /**
     * Return the progress bar in the display.
     *
     * @return the progress bar
     */
    public final StatusBar getProgress() {
        return progress;
    }

    /** {@inheritDoc} */
    public final void boardChange() {
        display.repaint();
    }

    /**
     * Set the display's status string to the human.
     *
     * @param status string to display
     */
    public final void setStatus(final String status) {
        progress.setStatus(status);
    }

    /**
     * Used for manaing menu events.
     */
    private class MenuHandler implements ActionListener {

        /** The "Game" menu. */
        private JMenu game;

        /** The "Action" menu. */
        private JMenu action;

        /** The parent chess frame, for callbacks. */
        private ChessFrame frame;

        /** Is the menu in game mode? */
        private boolean gameMode;

        /**
         * Create the menu handler.
         *
         * @param parent parent frame
         */
        public MenuHandler(final ChessFrame parent) {
            frame = parent;
        }

        /** {@inheritDoc} */
        public final void actionPerformed(final ActionEvent e) {
            if ("New Game".equals(e.getActionCommand())) {
                frame.newGame();
            } else if ("Undo".equals(e.getActionCommand())) {
                frame.undo();
            } else if ("Exit".equals(e.getActionCommand())) {
                System.exit(0);
            }
        }

        /**
         * Set up the menu bar.
         */
        public final void setUpMenu() {
            JMenuBar menuBar = new JMenuBar();

            game = new JMenu("Game");
            game.setMnemonic('G');
            JMenuItem newGame = new JMenuItem("New Game");
            newGame.addActionListener(this);
            newGame.setMnemonic('N');
            game.add(newGame);
            game.add(new JSeparator());
            JMenuItem exitGame = new JMenuItem("Exit");
            exitGame.addActionListener(this);
            exitGame.setMnemonic('x');
            game.add(exitGame);
            menuBar.add(game);

            action = new JMenu("Action");
            action.setMnemonic('A');
            action.setEnabled(false);
            JMenuItem undo = new JMenuItem("Undo");
            undo.setMnemonic('U');
            undo.addActionListener(this);
            action.add(undo);
            menuBar.add(action);

            setJMenuBar(menuBar);
        }

        /**
         * Put the menu in or out of game made.
         *
         * @param mode tell menu which mode it's in
         */
        public void gameMode(final boolean mode) {
            action.setEnabled(mode);
            gameMode = mode;
        }
    }
}
