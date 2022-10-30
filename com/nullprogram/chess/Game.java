package com.nullprogram.chess;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

/**
 * Drives a game of chess, given players and a board.
 *
 * This will, in turn, inform players of their turn and wait for them
 * to respond with a move.
 */
public class Game implements Runnable {

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("com.nullprogram.chess.Game");

    /** Conversion from milliseconds to seconds. */
    private static final double MSEC_TO_SEC = 1000.0;

    /** The board being used for this game. */
    private Board board;

    /** The player playing white. */
    private Player white;

    /** The player playing black. */
    private Player black;

    /** Whose turn it is right now. */
    private Piece.Side turn;

    /** Current status message. */
    private String status = "";

    /** Current progress (AI). */
    private float progress;

    /** Time when progress bar was restarted. */
    private long progressStart;

    /** Time of last progress bar update. */
    private long progressUpdate;

    /** Normalized ETA value. */
    private double etaUnit;

    /** Weighting of old value in timing estimates. */
    private static final double ALPHA = 0.4;

    /** Set to true when the board is in a completed state. */
    private volatile Boolean done = false;

    /** When the game is done, this is the winner. */
    private Piece.Side winner;

    /** List of event listeners. */
    private final Collection<GameListener> listeners =
        new CopyOnWriteArraySet<GameListener>();

    /**
     * Hidden constructor.
     */
    protected Game() {
    }

    /**
     * Create a new game with the given board and players.
     *
     * @param gameBoard   the game board
     */
    public Game(final Board gameBoard) {
        board = gameBoard;
    }

    /**
     * Seat the given players at this game.
     *
     * @param whitePlayer the player playing white
     * @param blackPlayer the player playing black
     */
    public final void seat(final Player whitePlayer,
                           final Player blackPlayer) {
        white = whitePlayer;
        black = blackPlayer;
    }

    /**
     * End the running game.
     */
    public final void end() {
        listeners.clear();
        winner = null;
        done = true;
    }

    /**
     * Begin the game.
     */
    public final void begin() {
        done = false;
        turn = Piece.Side.BLACK;
        callGameListeners(GameEvent.TURN);
        new Thread(this).start();
    }

    @Override
    public final void run() {
        while (!done) {
            /* Determine who's turn it is. */
            Player player;
            if (turn == Piece.Side.WHITE) {
                turn = Piece.Side.BLACK;
                setStatus("Black's turn.");
                player = black;
            } else {
                turn = Piece.Side.WHITE;
                setStatus("White's turn.");
                player = white;
            }

            /* Fetch the move from the player. */
            Move move = player.takeTurn(getBoard(), turn);
            board.move(move);
            setProgress(0);
            if (done) {
                /* Game may have ended abruptly during the player's
                 * potentially lengthy turn. */
                return;
            }

            /* Check for the end of the game. */
            Piece.Side opp = Piece.opposite(turn);
            if (board.checkmate(opp)) {
                done = true;
                if (opp == Piece.Side.BLACK) {
                    setStatus("White wins!");
                    winner = Piece.Side.WHITE;
                } else {
                    setStatus("Black wins!");
                    winner = Piece.Side.BLACK;
                }
                callGameListeners(GameEvent.GAME_END);
                return;
            } else if (board.stalemate(opp)) {
                done = true;
                setStatus("Stalemate!");
                winner = null;
                callGameListeners(GameEvent.GAME_END);
                return;
            }
            callGameListeners(GameEvent.TURN);
        }
    }

    /**
     * Get the board that belongs to this game.
     *
     * @return the game's board
     */
    public final Board getBoard() {
        return board.copy();
    }

    /**
     * Add a new event listener.
     *
     * @param listener the new event listener
     */
    public final void addGameListener(final GameListener listener) {
        listeners.add(listener);
    }

    /**
     * Call all of the game event listeners.
     *
     * @param type the type of event that occured
     */
    private void callGameListeners(final int type) {
        for (GameListener listener : listeners) {
            listener.gameEvent(new GameEvent(this, type));
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

    /**
     * Set the Game's current status message.
     *
     * @param message  new status message
     */
    public final void setStatus(final String message) {
        LOG.info("status: " + message);
        if (message == null) {
            throw new IllegalArgumentException();
        }
        status = message;
        callGameListeners(GameEvent.STATUS);
    }

    /**
     * Get the Game's current status message.
     *
     * @return the current status message
     */
    public final String getStatus() {
        return status;
    }

    /**
     * Set the Game's current progress status.
     *
     * @param value  current progress (0.0-1.0)
     */
    public final void setProgress(final float value) {
        LOG.finest("Game progress: " + value);
        progress = value;
        if (value == 0) {
            progressStart = System.currentTimeMillis();
            etaUnit = 0;
        } else {
            long diff = System.currentTimeMillis() - progressStart;
            double unit = diff / value / MSEC_TO_SEC;
            if (etaUnit == 0) {
                etaUnit = unit;
            } else {
                etaUnit = ALPHA * etaUnit + (1 - ALPHA) * unit;
            }
        }
        progressUpdate = System.currentTimeMillis();
        callGameListeners(GameEvent.STATUS);
    }

    /**
     * Estimated time remaining on the progress bar.
     *
     * @return the estimated seconds remaining
     */
    public final double getETA() {
        long now = System.currentTimeMillis();
        if (progress > 0) {
            double diff = (now - progressUpdate) / MSEC_TO_SEC;
            double t = (etaUnit * (1.0 - progress)) - diff;
            if (t < 0) {
                return 0;
            } else {
                return t;
            }
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Return the Game's current progress.
     *
     * @return the current progress (0.0-1.0)
     */
    public final float getProgress() {
        return progress;
    }
}
