package com.nullprogram.chess;

import java.util.logging.Logger;

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

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("com.nullprogram.chess.ChessApplet");

    /** Version for object serialization. */
    private static final long serialVersionUID = 34863129470926196L;

    @Override
    public void init() {
        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (Exception e) {
            LOG.warning("Failed to set 'Look and Feel'.");
        }

        StandardBoard board = new StandardBoard();
        BoardPanel panel = new BoardPanel(board);
        add(panel);
        Game game = new Game(board);
        game.seat(panel, new Minimax(game));
        game.addGameListener(this);
        game.addGameListener(panel);
        game.begin();
    }

    @Override
    public void gameEvent(final GameEvent e) {
        if (e.getGame().isDone()) {
            String message;
            Piece.Side winner = e.getGame().getWinner();
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
