package com.nullprogram.chess.ai;

import java.util.HashMap;
import java.util.Collections;

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

import com.nullprogram.chess.gui.StatusBar;

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

    /** The current best known move. */
    private Move selected;

    /** Best move score, corresponding to the selected move. */
    private double bestScore;

    /** Used to display AI's progress. */
    private StatusBar progress;

    /** Time AI turns. */
    private long startTime;

    /** The search threads. */
    private Thread[] threads;

    /** Maximum search depth. */
    static final int MAX_DEPTH = 3;

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
    public Minimax(final Board gameBoard, final StatusBar status) {
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
        moves = board.allMoves(side);
        Collections.shuffle(moves);

        /* Initialize the shared structures. */
        progress.setValue(0);
        progress.setMaximum(moves.size() - 1);
        progress.setStatus("Thinking ...");
        startTime = System.currentTimeMillis();
        selected = null;
        bestScore = Double.NEGATIVE_INFINITY;

        /* Spin off threads to evaluate each move's tree. */
        int threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println("AI using " + threadCount + " threads.");
        threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(this);
            threads[i].start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        long time = (System.currentTimeMillis() - startTime);
        System.out.println("Took " + (time / MILLI) + " seconds.");
        game.move(selected);
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
    }

    /** {@inheritDoc} */
    public final void run() {
        Board b = board.copy();
        for (Move move = getNextMove(); move != null; move = getNextMove()) {
            b.move(move);
            double v = search(b, MAX_DEPTH, Piece.opposite(side),
                              Double.NEGATIVE_INFINITY, -bestScore);
            b.undo();
            report(move, -v);
        }
    }

    /**
     * Recursive move searching.
     *
     * @param b     board to search
     * @param depth current depth
     * @param s     side for current move
     * @param alpha lower bound to check
     * @param beta  upper bound to check
     * @return      best valuation found at lowest depth
     */
    private double search(final Board b, final int depth, final Piece.Side s,
                          final double alpha, final double beta) {
        if (depth == 0) {
            return valuate(b);
        }
        Piece.Side opps = Piece.opposite(s);  // opposite side
        double best = alpha;
        MoveList list = b.allMoves(s);
        for (Move move : list) {
            b.move(move);
            best = Math.max(best, -search(b, depth - 1, opps, -beta, -best));
            b.undo();
            /* alpha-beta prune */
            if (beta <= best) {
                return best;
            }
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
        double material = materialValue(b);
        return material;
    }

    /**
     * Add up the material value of the board only.
     *
     * @param b board to be evaluated
     * @return  material value of the board
     */
    private double materialValue(final Board b) {
        double value = 0;
        for (int y = 0; y < b.getHeight(); y++) {
            for (int x = 0; x < b.getWidth(); x++) {
                Position pos = new Position(x, y);
                Piece p = b.getPiece(pos);
                if (p != null) {
                    value += values.get(p.getClass()) * p.getSide().value();
                }
            }
        }
        return value * side.value();
    }
}
