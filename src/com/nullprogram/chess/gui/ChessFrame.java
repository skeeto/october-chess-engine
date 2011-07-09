package com.nullprogram.chess.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.Player;
import com.nullprogram.chess.Game;

import com.nullprogram.chess.Chess;
import com.nullprogram.chess.GameListener;
import com.nullprogram.chess.boards.EmptyBoard;
import com.nullprogram.chess.pieces.ImageServer;

/**
 * The JFrame that contains all GUI elements.
 */
public class ChessFrame extends JFrame
    implements ComponentListener, GameListener {

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
        super(Chess.getTitle());
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(ImageServer.getTile("King-WHITE"));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        handler = new MenuHandler(this);
        handler.setUpMenu();

        display = new BoardPanel(new EmptyBoard());
        progress = new StatusBar(null);
        add(display);
        add(progress);
        pack();

        addComponentListener(this);
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
        if (game != null) {
            game.end();
        }
        game = newGame;
        Board board = game.getBoard();
        board.addBoardListener(display);
        display.setBoard(board);
        display.invalidate();
        setSize(getPreferredSize());
        handler.gameMode(true);

        progress.setGame(game);
        game.addListener(this);
        game.begin();
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
     * Used for manaing menu events.
     */
    private class MenuHandler implements ActionListener {

        /** The "Game" menu. */
        private JMenu game;

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

        @Override
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

            setJMenuBar(menuBar);
        }

        /**
         * Put the menu in or out of game made.
         *
         * @param mode tell menu which mode it's in
         */
        public void gameMode(final boolean mode) {
            gameMode = mode;
        }
    }

    @Override
    public final void componentResized(final ComponentEvent e) {
        if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) {
            /* If the frame is maxmized, the battle has been lost. */
            return;
        }
        double ratio = display.getRatio();
        double barh = progress.getPreferredSize().getHeight();
        Container p = getContentPane();
        Dimension d = null;
        if (p.getWidth() * ratio < (p.getHeight() - barh)) {
            d = new Dimension((int) ((p.getHeight() - barh) * ratio),
                              p.getHeight());
        } else if (p.getWidth() * ratio > (p.getHeight() - barh)) {
            d = new Dimension(p.getWidth(),
                              (int) (p.getWidth() / ratio + barh));
        }
        if (d != null) {
            p.setPreferredSize(d);
            pack();
        }
    }

    @Override
    public final void gameEvent(final Game e) {
        progress.repaint();
        display.repaint();
    }

    @Override
    public void componentHidden(final ComponentEvent e) {
    }

    @Override
    public void componentMoved(final ComponentEvent e) {
    }

    @Override
    public void componentShown(final ComponentEvent e) {
    }
}
