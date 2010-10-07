package com.nullprogram.chess;

import javax.swing.JFrame;

import com.nullprogram.chess.gui.BoardPanel;
import com.nullprogram.chess.boards.StandardBoard;

/**
 * Main class for the Chess game application.
 */
public final class Chess {

    /**
     * Hidden constructor.
     */
    protected Chess() {
    }

    /**
     * The main method of the Chess game application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        Board board = new StandardBoard();
        BoardPanel display = new BoardPanel(board);

        JFrame frame = new JFrame("Chess");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(display);
        frame.pack();
        frame.setVisible(true);

        /* Set up a hot-seat game */
        Game game = new Game(board, display, display);
    }
}
