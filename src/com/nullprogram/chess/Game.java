package com.nullprogram.chess;

import java.util.List;
import java.util.ArrayList;

import com.nullprogram.chess.gui.ChessFrame;

/**
 * Drives a game of chess, given players and a board.
 *
 * This will, in turn, inform players of their turn and wait for them
 * to respond with a move.
 */
public class Game implements Runnable {

    /** Display frame. */
    private ChessFrame frame;

    /** The board being used for this game. */
    private Board board;

    /** The player playing white. */
    private Player white;

    /** The player playing black. */
    private Player black;

    /** Whose turn it is right now. */
    private Piece.Side turn;

    /** Set to true when the board is in a completed state. */
    private volatile Boolean done;

    /** When the game is done, this is the winner. */
    private Piece.Side winner;

    /** List of event listeners. */
    private List<GameListener> listeners;

    /**
     * Hidden constructor.
     */
    protected Game() {
    }

    /**
     * Create a new game with the given board and players.
     *
     * @param display     display for this game
     * @param gameBoard   the game board
     * @param whitePlayer the player playing white
     * @param blackPlayer the player playing black
     */
    public Game(final ChessFrame display,
                final Board gameBoard,
                final Player whitePlayer,
                final Player blackPlayer) {
        done = false;
        frame = display;
        board = gameBoard;
        white = whitePlayer;
        black = blackPlayer;
        white.setGame(this);
        black.setGame(this);
        listeners = new ArrayList<GameListener>();
    }

    /**
     * Begin the game.
     */
    public final void begin() {
        done = false;
        turn = Piece.Side.BLACK;
        callListeners();
        if (!done) {
            (new Thread(this)).start();
        }
    }

    /**
     * End the running game.
     */
    public final void end() {
        winner = null;
        done = true;
    }

    /**
     * Perform a turn of the game.
     *
     * @param move the move action to take
     */
    public final void move(final Move move) {
        if (done) {
            return;
        }
        board.move(move);
        Piece.Side opp = Piece.opposite(turn);
        if (board.checkmate(opp) || board.stalemate(opp)) {
            if (opp == Piece.Side.BLACK) {
                if (frame != null) {
                    frame.setStatus("White wins!");
                }
                winner = Piece.Side.WHITE;
            } else if (opp == Piece.Side.WHITE) {
                if (frame != null) {
                    frame.setStatus("Black wins!");
                }
                winner = Piece.Side.BLACK;
            } else {
                if (frame != null) {
                    frame.setStatus("Stalemate!");
                }
                winner = null;
            }
            if (frame != null) {
                frame.endGame();
            }
            done = true;
            white.setBoard(board);
            black.setBoard(board);
            callListeners();
            return;
        }
        callListeners();
        if (!done) {
            (new Thread(this)).start();
        }
    }

    /**
     * The thread that fires off the next player.
     */
    public final void run() {
        switchTurns();
    }

    /**
     * Switch the turn variable and inform the player.
     */
    private void switchTurns() {
        if (turn == Piece.Side.WHITE) {
            turn = Piece.Side.BLACK;
            turnStatus();
            black.setActive(board.copy(), turn);
        } else {
            turn = Piece.Side.WHITE;
            turnStatus();
            white.setActive(board.copy(), turn);
        }
    }

    /**
     * Try to undo the last move in the game.
     */
    public final void undo() {
        board.undo();
        switchTurns();
        /* Still need to inform active player of this! */
    }

    /**
     * Display the current turn status.
     */
    private void turnStatus() {
        String status;
        if (turn == Piece.Side.WHITE) {
            status = "White's turn.";
        } else {
            status = "Black's turn.";
        }
        if (frame != null) {
            frame.getProgress().setValue(0);
            frame.setStatus(status);
        }
    }

    /**
     * Get the board that belongs to this game.
     *
     * @return the game's board
     */
    public final Board getBoard() {
        return board;
    }

    /**
     * Add a new event listener.
     *
     * @param listener the new event listener
     */
    public final void addListener(final GameListener listener) {
        listeners.add(listener);
    }

    /**
     * Call all of the game event listeners.
     */
    private void callListeners() {
        for (GameListener listener : listeners) {
            listener.gameEvent(this);
        }
    }

    /**
     * Ask if the game is complete.
     *
     * @return true if the game is complete
     */
    public final boolean isDone() {
        return done;
    }

    /**
     * Return the winner of this game.
     *
     * @return the winner for this game
     */
    public final Piece.Side getWinner() {
        return winner;
    }
}
