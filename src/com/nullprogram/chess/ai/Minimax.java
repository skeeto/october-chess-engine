package com.nullprogram.chess.ai;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;

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

/**
 * Minimax Chess AI player.
 *
 * This employs the dumb minimax algorithm to search the game tree for
 * moves. The board is currently only evaluated only by the pieces
 * present, not their positions.
 */
public class Minimax implements Player {
    /** This class's Logger. */
    private static final Logger LOG = Logger.getLogger("ai.Minimax");

    /** The number of threads to use. */
    public static final int NTHREADS
    = Runtime.getRuntime().availableProcessors();

    private static final HelperSocket CPU = new HelperSocket(null);

    private static final LinkedBlockingQueue<HelperSocket> helpers
        = new LinkedBlockingQueue<HelperSocket>();

    /** Board the AI will be playing on. */
    private Board board;

    /** Local friendly game controller. */
    private Game game;

    /** Side this AI plays. */
    private Piece.Side side;

    /** Best move, the selected move. */
    private volatile Move bestMove;

    /** Time AI turns. */
    private long startTime;

    /** Thread manager. */
    private final Executor executor = Executors.newCachedThreadPool();

    /** Values of each piece. */
    private Map<Class, Double> values;

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
     * Create the default Minimax.
     */
    public Minimax() {
        this("default");
    }

    /**
     * Create a new AI from a given properties name.
     *
     * @param name      name of configuration to use
     */
    public Minimax(final String name) {
        this(getConfig(name));
    }

    /**
     * Create a new AI for the given board.
     *
     * @param props     properties for this player
     */
    public Minimax(final Properties props) {
        if (helpers.size() == 0) {
            for (int i = 0; i < NTHREADS; i++) {
                helpers.add(CPU);
            }
        }

        values = new HashMap<Class, Double>();
        config = props;

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

        maxDepth = (int) Double.parseDouble(config.getProperty("depth"));
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
    public static Properties getConfig(final String name) {
        Properties props = new Properties();
        String filename = name + ".properties";
        try {
            props.load(Minimax.class.getResourceAsStream(filename));
        } catch (java.io.IOException e) {
            LOG.warning("Failed to load AI config: " + name + ": " + e);
        }
        return props;
    }

    @Override
    public final void setGame(final Game currentGame) {
        game = currentGame;
    }

    @Override
    public final void setActive(final Board curBoard,
                                final Piece.Side currentSide) {
        board = curBoard;
        side = currentSide;

        /* Gather up every move. */
        MoveList moves = board.allMoves(side, true);
        Collections.shuffle(moves);

        /* Initialize the shared structures. */
        if (game != null) {
            game.setProgress(0);
            game.setStatus("Thinking ...");
        }
        startTime = System.currentTimeMillis();

        /* Spin off threads to evaluate each move's tree. */
        CompletionService<Move> service
        = new ExecutorCompletionService<Move>(executor);
        int submitted = 0;
        bestMove = null;
        for (final Move move : moves) {
            final Board callboard = board.copy();
            service.submit(new Callable<Move>() {
                public Move call() {
                    HelperSocket socket = null;
                    try {
                        socket = helpers.take();
                    } catch (InterruptedException e) {
                        LOG.warning("move evaluation interupted");
                        return move;
                    }
                    if (socket == CPU) {
                        /* Special dummy CPU "socket". */
                        LOG.fine("CPU evaluation");
                        callboard.move(move);
                        double beta = Double.POSITIVE_INFINITY;
                        if (bestMove != null) {
                            beta = -bestMove.getScore();
                        }
                        double v = search(callboard, maxDepth - 1,
                                          Piece.opposite(side),
                                          Double.NEGATIVE_INFINITY, beta);
                        move.setScore(-v);
                        helpers.add(CPU);
                        return move;
                    } else {
                        try {
                            ObjectOutputStream out = socket.getOut();
                            out.writeObject(callboard);
                            out.writeObject(move);
                            out.writeObject(side);
                            out.flush();
                            ObjectInputStream in = socket.getIn();
                            Move ret = (Move) in.readObject();
                            helpers.add(socket);
                            return ret;
                        } catch (Exception e) {
                            helpers.add(socket);
                            LOG.severe("helper search failed");
                        }
                    }
                    move.setScore(Double.POSITIVE_INFINITY);
                    return move;
                }
            });
            submitted++;
        }

        /* Gather up results and pick the best move. */
        for (int i = 0; i < submitted; i++) {
            try {
                Move m = service.take().get();
                if (bestMove == null || m.getScore() > bestMove.getScore()) {
                    bestMove = m;
                }
            } catch (Exception e) {
                /* This move was unevaluated. */
                LOG.severe("move went unevaluated: " + e);
            }
            if (game != null) {
                game.setProgress(i / (1.0f * (submitted - 1)));
            }
        }

        long time = (System.currentTimeMillis() - startTime);
        LOG.info("AI took " + (time / MILLI) + " seconds ("
                 + NTHREADS + " threads)");
        game.move(bestMove);
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
    protected double search(final Board b, final int depth, final Piece.Side s,
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

    @Override
    public final void setBoard(final Board b) {
        board = b;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setSide(Piece.Side s) {
        side = s;
    }

    public static void addHelper(HelperSocket socket) {
        helpers.add(socket);
    }
}
