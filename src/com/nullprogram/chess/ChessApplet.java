package com.nullprogram.chess;

import javax.swing.UIManager;
import javax.swing.JApplet;
import javax.swing.JOptionPane;

import com.nullprogram.chess.gui.BoardPanel;

import com.nullprogram.chess.boards.StandardBoard;

import com.nullprogram.chess.ai.Minimax;

/**
 * Applet that runs a game versus the computer, no other options.
 */
public final class ChessApplet extends JApplet implements GameListener {

    /** Version for object serialization. */
    private static final long serialVersionUID = 34863129470926196L;

    /** {@inheritDoc} */
    public void init() {
        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (Exception e) {
            System.out.println("Failed to set 'Look and Feel'.");
        }

        StandardBoard board = new StandardBoard();
        BoardPanel panel = new BoardPanel(board);
        add(panel);
        Game game = new Game(null, board, panel, new Minimax(null));
        game.addListener(this);
        game.begin();
    }

    /** {@inheritDoc} */
    public void gameEvent(final Game game) {
        if (game.isDone()) {
            String message;
            Piece.Side winner = game.getWinner();
            if (winner == Piece.Side.WHITE) {
                message = "White wins";
            } else if (winner == Piece.Side.BLACK) {
                message = "Black wins";
            } else {
                message = "Stalemate";
            }
            JOptionPane.showMessageDialog(null, message);
        }
    }
}
