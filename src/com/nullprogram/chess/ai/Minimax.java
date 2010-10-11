package com.nullprogram.chess.ai;

import java.util.HashMap;
import java.util.Collections;
import java.util.Properties;

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
import com.nullprogram.chess.pieces.Chancellor;
import com.nullprogram.chess.pieces.Archbishop;

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

    /** Values of each piece. */
    private HashMap<Class, Double> values;

    /** Divisor for milliseconds. */
    static final double MILLI = 1000.0;

    /** Current AI configuration. */
    private Properties config;

    /** Maximum depth (configured). */
    private int maxDepth;

    /** Material score weight (configured). */
    private double wMaterial;

    /** King safety score weight (configured). */
    private double wSafety;

    /** Mobility score weight (configured). */
    private double wMobility;

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
        this(gameBoard, status, "default");
    }

    /**
     * Create a new AI for the given board.
     *
     * @param gameBoard the board to be displayed
     * @param status    GUI progress bar
     * @param name name of configuration to use
     */
    public Minimax(final Board gameBoard, final StatusBar status,
                   final String name) {
        board = gameBoard;
        values = new HashMap<Class, Double>();
        progress = status;

        config = getConfig(name);

        /* Piece values */
        values.put((new Pawn(side)).getClass(),
                   Double.parseDouble(config.getProperty("Pawn")));
        values.put((new Knight(side)).getClass(),
                   Double.parseDouble(config.getProperty("Knight")));
        values.put((new Bishop(side)).getClass(),
                   Double.parseDouble(config.getProperty("Bishop")));
        values.put((new Rook(side)).getClass(),
                   Double.parseDouble(config.getProperty("Rook")));
        values.put((new Queen(side)).getClass(),
                   Double.parseDouble(config.getProperty("Queen")));
        values.put((new King(side)).getClass(),
                   Double.parseDouble(config.getProperty("King")));
        values.put((new Chancellor(side)).getClass(),
                   Double.parseDouble(config.getProperty("Chancellor")));
        values.put((new Archbishop(side)).getClass(),
                   Double.parseDouble(config.getProperty("Archbishop")));

        maxDepth = Integer.parseInt(config.getProperty("depth"));
        wMaterial = Double.parseDouble(config.getProperty("material"));
        wSafety = Double.parseDouble(config.getProperty("safety"));
        wMobility = Double.parseDouble(config.getProperty("mobility"));
    }

    /**
     * Get the configuration.
     *
     * @param name name of the configuration to load
     * @return the configuration
     */
    private Properties getConfig(final String name) {
        Properties props = new Properties();
        String filename = name + ".properties";
        try {
            props.load(Minimax.class.getResourceAsStream(filename));
        } catch (java.io.IOException e) {
            System.out.println(e);
            /* Do something else here sometime. */
        }
        return props;
    }

    /** {@inheritDoc} */
    public final void setGame(final Game currentGame) {
        game = currentGame;
    }

    /** {@inheritDoc} */
    public final void setActive(final Piece.Side currentSide) {
        side = currentSide;

        /* Gather up every move. */
        moves = board.allMoves(side, true);
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
            double v = search(b, maxDepth, Piece.opposite(side),
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
        MoveList list = b.allMoves(s, true);
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
        double kingSafety = kingInsafetyValue(b);
        double mobility = mobilityValue(b);
        return material * wMaterial
               + kingSafety * wSafety
               + mobility * wMobility;
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

    /**
     * Determine the safety of each king. Higher is worse.
     *
     * @param b board to be evaluated
     * @return  king insafety score
     */
    private double kingInsafetyValue(final Board b) {
        return kingInsafetyValue(b, Piece.opposite(side))
               - kingInsafetyValue(b, side);
    }

    /**
     * Helper function: determine safety of a single king.
     *
     * @param b board to be evaluated
     * @param s side of king to be checked
     * @return king insafety score
     */
    private double kingInsafetyValue(final Board b, final Piece.Side s) {
        /* Trace lines away from the king and count the spaces. */
        Position king = b.findKing(s);
        if (king == null) {
            /* Weird, but may happen during evaluation. */
            return Double.POSITIVE_INFINITY;
        }
        MoveList list = new MoveList(b, false);
        /* Take advantage of the Rook and Bishop code. */
        Rook.getMoves(b.getPiece(king), list);
        Bishop.getMoves(b.getPiece(king), list);
        return list.size();
    }

    /**
     * Mobility score for this board.
     *
     * @param b board to be evaluated
     * @return  score for this board
     */
    private double mobilityValue(final Board b) {
        return b.allMoves(side, false).size()
               - b.allMoves(Piece.opposite(side), false).size();
    }
}
