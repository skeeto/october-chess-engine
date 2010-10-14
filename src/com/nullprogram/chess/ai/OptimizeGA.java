package com.nullprogram.chess.ai;

import java.util.Random;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Game;
import com.nullprogram.chess.GameListener;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.Player;

import com.nullprogram.chess.boards.StandardBoard;

/**
 * Alternate main class for optimizing AI parameters via genetic algorithm.
 */
public class OptimizeGA implements GameListener {

    /** Random number generator.  */
    private static Random rng;

    /** Maximum range for piece values. */
    static final double PIECE_RANGE = 10.0;

    /** Locked-in dpeth: we don't change this in the population. */
    static final int DEPTH = 4;

    /** Maximum number of moves. */
    static final int MAX_MOVES = 75;

    /**
     * Hidden constructor.
     */
    protected OptimizeGA() {
        launch(create(), create());
    }

    /**
     * Rather than start the GUI, run undisplayed games.
     *
     * @param args input arguments
     */
    public static void main(final String[] args) {
        rng = new Random();
        new OptimizeGA();
    }

    /**
     * Launch a game with players using the given configurations.
     *
     * @param whiteConf config for the white player
     * @param blackConf config for the black player
     */
    private void launch(final Config whiteConf, final Config blackConf) {
        Player white = new Minimax(null, whiteConf.getProperties());
        Player black = new Minimax(null, whiteConf.getProperties());
        Board board = new StandardBoard();
        Game game = new Game(null, board, white, black);
        game.addListener(this);
        game.begin();
    }

    /** {@inheritDoc} */
    public final void gameEvent(final Game game) {
        if (game.isDone()) {
            System.out.println("Game complete: ");
            if (game.getWinner() == Piece.Side.WHITE) {
                System.out.println("White wins.");
            } else if (game.getWinner() == Piece.Side.BLACK) {
                System.out.println("White wins.");
            } else {
                System.out.println("Stalemate.");
            }
        } else if (game.getBoard().moveCount() > MAX_MOVES) {
            System.out.println("Game timeout!");
            game.end();
        }
    }

    /**
     * Randomly create a new configuration.
     *
     * @return a randomly generated config
     */
    private static Config create() {
        Config conf = new Config();
        conf.put("depth", (double) DEPTH);
        String[] pieces = {"Pawn", "Knight", "Bishop", "Rook", "Queen",
                           "King", "Chancellor", "Archbishop"};
        for (String piece : pieces) {
            Double v = rng.nextDouble() * PIECE_RANGE;
            if ("King".equals(piece)) {
                /* The king has a much larger value range. */
                v *= PIECE_RANGE * PIECE_RANGE;
            }
            conf.put(piece, v);
        }
        conf.put("material", rng.nextDouble());
        conf.put("safety", rng.nextDouble());
        conf.put("mobility", rng.nextDouble());
        return conf;
    }
}
