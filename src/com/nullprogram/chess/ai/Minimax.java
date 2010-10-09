package com.nullprogram.chess.ai;

import java.util.HashMap;

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

public class Minimax implements Player, Runnable {

    /** Board the AI will be playing on. */
    private Board board;

    /** Local friendly game controller. */
    private Game game;

    /** Side this AI plays. */
    private Piece.Side side;

    /** Maximum search depth. */
    static final int MAX_DEPTH = 3;

    /** Maximum search depth. */
    static final double INF = 10000;

    private HashMap<Class, Double> values;

    /**
     * Hidden constructor.
     */
    protected Minimax() {
    }

    /**
     * Create a new AI for the given board.
     *
     * @param displayBoard the board to be displayed
     */
    public Minimax(final Board gameBoard) {
        board = gameBoard;
        values = new HashMap<Class, Double>();

        /* Piece values */
        values.put((new Pawn(side)).getClass(),   1.0);
        values.put((new Knight(side)).getClass(), 3.0);
        values.put((new Bishop(side)).getClass(), 3.0);
        values.put((new Rook(side)).getClass(),   5.0);
        values.put((new Queen(side)).getClass(),  9.0);
        values.put((new King(side)).getClass(),   1000.0);
    }

    /** {@inheritDoc} */
    public void setGame(Game currentGame) {
        game = currentGame;
    }

    /** {@inheritDoc} */
    public final void setActive(final Piece.Side currentSide) {
        side = currentSide;
        (new Thread(this)).start();
    }

    /** {@inheritDoc} */
    public void run() {
        /* AI needs to eventually be running this on a copy of the board. */
        System.out.println("AI thread ... GO!");
        MoveList list = new MoveList(board, false);
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                Position pos = new Position(x, y);
                Piece p = board.getPiece(pos);
                if (p != null && p.getSide() == side) {
                    /* Gather up every move. */
                    list.addAll(p.getMoves(true));
                }
            }
        }

        /* Evaluate the tree under each move. */
        double best = 0;
        Move selected = null;
        for (Move move : list) {
            board.move(move);
            double v = search(board, Piece.opposite(side), MAX_DEPTH);
            if (selected == null || v > best) {
                selected = move;
                best = v;
            } else if (v == best) {
                /* randomize this eventually */
            }
            board.undo();
        }
        System.out.println("AI is done!");
        game.move(selected);
    }

    /**
     * Recursive move searching.
     *
     * @param b     board to search
     * @param depth current depth
     * @return      best valuation found at lowest depth
     */
    private double search(Board b, Piece.Side s, int depth) {
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
                    MoveList moves = p.getMoves(true);
                    /* Try every move. */
                    for (Move move : moves) {
                        b.move(move);
                        double value = search(b, Piece.opposite(s), depth - 1);
                        value *= invert;
                        if (best == null || value > best) {
                            best = value;
                        } else if (value == best) {
                            /* randomize this eventually */
                        }
                        b.undo();
                    }
                }
            }
        }
        if (best == null) {
            best = INF;
        }
        return best;
    }

    /**
     * Determine value of this board.
     *
     * @param b board to be valuated
     * @return  valuation of this board
     */
    private double valuate(Board b) {
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
