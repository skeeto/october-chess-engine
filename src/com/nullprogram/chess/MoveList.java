package com.nullprogram.chess;

import java.util.ArrayList;

/**
 * Safe list of moves.
 *
 * Before a move is added it can be checked for some basic validity.
 */
public class MoveList extends ArrayList<Move> {

    /** Versioning for object serialization. */
    private static final long serialVersionUID = 1L;

    /** The board used to verify positions before adding them. */
    private Board board;

    /**
     * Create a new move list relative to a board.
     *
     * @param verifyBoard the board to be used
     */
    public MoveList(final Board verifyBoard) {
        board = verifyBoard;
    }

    /**
     * Add move to list if piece can legally move there (no capture).
     *
     * @param move move to be added
     * @return     true if position was added
     */
    public final boolean addMove(final Move move) {
        if (board.isFree(move.getDest())) {
            super.add(move);
            return true;
        }
        return false;
    }

    /**
     * Add move to list if piece can move <i>or</i> capture at destination.
     *
     * @param move position to be added
     * @return     true if position was added
     */
    public final boolean addCapture(final Move move) {
        Piece p = board.getPiece(move.getOrigin());
        if (board.isFree(move.getDest(), p.getSide())) {
            super.add(move);
            return true;
        }
        return false;
    }

    /**
     * Add move to list only if the piece will perform a capture.
     *
     * @param move position to be added
     * @return     true if position was added
     */
    public final boolean addCaptureOnly(final Move move) {
        Piece p = board.getPiece(move.getOrigin());
        if (board.isFree(move.getDest(), p.getSide())
                && !board.isFree(move.getDest())) {
            super.add(move);
            return true;
        }
        return false;
    }

    /**
     * Return true if this list contains the position as a destination.
     *
     * @param pos destination position
     * @return    true if destination is present in list
     */
    public final boolean containsDest(Position pos) {
        for (Move move : this) {
            if (pos.equals(move.getDest())) {
                return true;
            }
        }
        return false;
    }
}
