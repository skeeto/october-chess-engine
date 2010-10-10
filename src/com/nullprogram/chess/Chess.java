package com.nullprogram.chess;

import javax.swing.UIManager;

import com.nullprogram.chess.gui.ChessFrame;
import com.nullprogram.chess.boards.StandardBoard;
import com.nullprogram.chess.ai.Minimax;

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
        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (Exception e) {
            /* Do nothing. */
        }

        Board board = new StandardBoard();
        ChessFrame frame = new ChessFrame(board);

        /* Set up a hot-seat game */
        Minimax ai = new Minimax(board, frame.getProgress());
        Game game = new Game(frame, board, frame.getPlayer(), ai);
    }
}
