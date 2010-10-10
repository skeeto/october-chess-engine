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
import com.nullprogram.chess.Player;

/**
 * The JFrame that contains all GUI elements.
 */
public class ChessFrame extends JFrame {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** The board display. */
    private BoardPanel display;

    /** The progress bar on the display. */
    private StatusBar progress;

    /** Subclass instance for dealing with the menu. */
    private MenuHandler handler;

    /**
     * Create a new ChessFrame for the given board.
     *
     * @param board the game board to display
     */
    public ChessFrame(final Board board) {
        super("Chess");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        handler = new MenuHandler();
        handler.setUpMenu();

        display = new BoardPanel(board);
        progress = new StatusBar();
        setStatus("Initializing.");
        add(display);
        add(progress);
        pack();

        setVisible(true);
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

        /** {@inheritDoc} */
        public final void actionPerformed(final ActionEvent e) {
            if ("New Game".equals(e.getActionCommand())) {
                System.out.println("New Game Dialog");
            } else if ("Undo".equals(e.getActionCommand())) {
                System.out.println("Undo");
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
            menuBar.add(action);

            setJMenuBar(menuBar);
        }
    }
}
