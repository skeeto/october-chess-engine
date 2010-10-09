package com.nullprogram.chess.ai;

import java.util.HashMap;

import javax.swing.JProgressBar;

import com.nullprogram.chess.Game;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Player;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;

import com.nullprogram.chess.pieces.Pawn;
import com.nullprogram.chess.pieces.Rook;
import com.nullprogram.chess.pieces.Knight;
import com.nullprogram.chess.pieces.Bishop;
import com.nullprogram.chess.pieces.Queen;
import com.nullprogram.chess.pieces.King;

/**
 * Minimax Chess AI player.
 *
 * This employs the dumb minimax algorithm to search the game tree for
 * moves. The board is currently only evaluated only by the pieces
 * present, not their positions.
 */
public class Minimax implements Player, Runnable {

    /** Board the AI will be playing on. */
    private Board board;

    /** Local friendly game controller. */
    private Game game;

    /** Side this AI plays. */
    private Piece.Side side;

    /** List of moves to be evaluated. */
    private MoveList moves;

    /** Number of moves left to be evaluated. */
    private int moveCount;

    /** The current best known move. */
    private Move selected;

    /** Best move score, corresponding to the selected move. */
    private double bestScore;

    /** Used to display AI's progress. */
    private JProgressBar progress;

    /** Time AI turns. */
    private long startTime;

    /** Maximum search depth. */
    static final int MAX_DEPTH = 3;

    /** Maximum search depth. */
    static final double INF = 10000;

    /** Values of each piece. */
    private HashMap<Class, Double> values;

    /** Value of a pawn. */
    static final double PAWN_VALUE = 1.0;

    /** Value of a knight. */
    static final double KNIGHT_VALUE = 3.0;

    /** Value of a bishop. */
    static final double BISHOP_VALUE = 3.0;

    /** Value of a rook. */
    static final double ROOK_VALUE = 5.0;

    /** Value of a queen. */
    static final double QUEEN_VALUE = 9.0;

    /** Value of a king. */
    static final double KING_VALUE = 1000.0;

    /** Divisor for milliseconds. */
    static final double MILLI = 1000.0;

    /**
     * Hidden constructor.
     */
    protected Minimax() {
    }

    /**
     * Create a new AI for the given board.
     *
     * @param gameBoard the board to be displayed
     * @param status    GUI progress bar
     */
    public Minimax(final Board gameBoard, final JProgressBar status) {
        board = gameBoard;
        values = new HashMap<Class, Double>();

        /* Piece values */
        values.put((new Pawn(side)).getClass(),   PAWN_VALUE);
        values.put((new Knight(side)).getClass(), KNIGHT_VALUE);
        values.put((new Bishop(side)).getClass(), BISHOP_VALUE);
        values.put((new Rook(side)).getClass(),   ROOK_VALUE);
        values.put((new Queen(side)).getClass(),  QUEEN_VALUE);
        values.put((new King(side)).getClass(),   KING_VALUE);

        progress = status;
    }

    /** {@inheritDoc} */
    public final void setGame(final Game currentGame) {
        game = currentGame;
    }

    /** {@inheritDoc} */
    public final void setActive(final Piece.Side currentSide) {
        side = currentSide;

        /* Gather up every move. */
        moves = new MoveList(board, false);
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                Position pos = new Position(x, y);
                Piece p = board.getPiece(pos);
                if (p != null && p.getSide() == side) {
                    moves.addAll(p.getMoves(true));
                }
            }
        }
        /* Eventually randomize this list, too. */

        /* Initialize the shared structures. */
        moveCount = moves.size();
        progress.setValue(0);
        progress.setMaximum(moves.size() - 1);
        progress.setString("Thinking ...");
        startTime = System.currentTimeMillis();
        selected = null;
        bestScore = 0;

        /* Spin off threads to evaluate each move's tree. */
        int threads = Runtime.getRuntime().availableProcessors();
        System.out.println("AI using " + threads + " threads.");
        for (int i = 0; i < threads; i++) {
            (new Thread(this)).start();
        }
    }

    /**
     * Hand off another move in the calling thread.
     *
     * @return an unevaluated move
     */
    public final synchronized Move getNextMove() {
        if (moves.isEmpty()) {
            return null;
        }
        progress.setValue(progress.getValue() + 1);
        return moves.pop();
    }

    /**
     * Thread calls in here to report its results.
     *
     * @param move  the move being reported
     * @param score the score of the move
     */
    public final synchronized void report(final Move move, final double score) {
        if (selected == null || score > bestScore) {
            bestScore = score;
            selected = move;
        }
        moveCount--;
        if (moveCount == 0) {
            /* All moves accounted for. */
            game.move(selected);
            progress.setString("Done.");
            long time = (System.currentTimeMillis() - startTime);
            System.out.println("Took " + (time / MILLI) + " seconds.");
        }
    }

    /** {@inheritDoc} */
    public final void run() {
        Board b = board.copy();
        for (Move move = getNextMove(); move != null; move = getNextMove()) {
            b.move(move);
            double v = search(b, Piece.opposite(side), MAX_DEPTH);
            report(move, v);
            b.undo();
        }
    }

    /**
     * Recursive move searching.
     *
     * @param b     board to search
     * @param depth current depth
     * @param s     side for current move
     * @return      best valuation found at lowest depth
     */
    private double search(final Board b, final Piece.Side s, final int depth) {
        if (depth == 0) {
            return valuate(b);
        }
        int invert;
        if (side == s) {
            invert = 1;
        } else {
            invert = -1;
        }
        Double best = null;
        for (int y = 0; y < b.getHeight(); y++) {
            for (int x = 0; x < b.getWidth(); x++) {
                Position pos = new Position(x, y);
                Piece p = b.getPiece(pos);
                if (p != null && p.getSide() == s) {
                    MoveList list = p.getMoves(true);
                    /* Try every move. */
                    for (Move move : list) {
                        b.move(move);
                        double value = search(b, Piece.opposite(s), depth - 1);
                        if (best == null || (value * invert) > best) {
                            best = value;
                        }
                        b.undo();
                    }
                }
            }
        }
        if (best == null) {
            best = INF * invert * -1;
        }
        return best;
    }

    /**
     * Determine value of this board.
     *
     * @param b board to be valuated
     * @return  valuation of this board
     */
    private double valuate(final Board b) {
        double value = 0;
        for (int y = 0; y < b.getHeight(); y++) {
            for (int x = 0; x < b.getWidth(); x++) {
                Position pos = new Position(x, y);
                Piece p = b.getPiece(pos);
                if (p != null) {
                    if (p.getSide() == side) {
                        value += values.get(p.getClass());
                    } else {
                        value -= values.get(p.getClass());
                    }
                }
            }
        }
        return value;
    }
}
