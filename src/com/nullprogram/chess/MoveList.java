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

    /** Should we check for check when verifying moves. */
    private boolean check;

    /**
     * Create a new move list relative to a board.
     *
     * @param verifyBoard the board to be used
     */
    public MoveList(final Board verifyBoard) {
        this(verifyBoard, true);
    }

    /**
     * Create a new move list relative to a board.
     *
     * @param checkCheck  check for check
     * @param verifyBoard the board to be used
     */
    public MoveList(final Board verifyBoard, final boolean checkCheck) {
        board = verifyBoard;
        check = checkCheck;
    }

    /**
     * Add move to list if piece can legally move there (no capture).
     *
     * @param move move to be added
     * @return     true if position was added
     */
    public final boolean addMove(final Move move) {
        if (board.isFree(move.getDest())) {
            if (!causesCheck(move)) {
                super.add(move);
                return true;
            }
            return true; // false only for a "blocking" move
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
            if (!causesCheck(move)) {
                super.add(move);
                return true;
            }
            return true; // false only for a "blocking" move
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
        if (board.isFree(move.getDest(), p.getSide()) &&
            !board.isFree(move.getDest()) &&
            !causesCheck(move)) {

            super.add(move);
            return true;
        }
        return false;
    }

    /**
     * Determine if move will cause check for the same side.
     *
     * @param move move to be tested
     * @return     true if move causes check
     */
    private boolean causesCheck(final Move move) {
        if (!check) {
            return false;
        }
        Piece p = board.getPiece(move.getOrigin());
        board.move(move);
        boolean ret = board.check(p.getSide());
        board.undo();
        return ret;
    }

    /**
     * Return true if this list contains the position as a destination.
     *
     * @param pos destination position
     * @return    true if destination is present in list
     */
    public final boolean containsDest(final Position pos) {
        for (Move move : this) {
            if (pos.equals(move.getDest())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the move containing the destination.
     *
     * @param dest destination position
     * @return     move containing given destination
     */
    public final Move getMoveByDest(final Position dest) {
        for (Move move : this) {
            if (dest.equals(move.getDest())) {
                return move;
            }
        }
        return null;
    }

    /**
     * Stack behavior: pop off the last element on return it.
     *
     * @return move popped off the stack
     */
    public final Move pop() {
        if (isEmpty()) {
            return null;
        }
        Move last = get(size() - 1);
        remove(size() - 1);
        return last;
    }

    /**
     * Stack behavior: peek at last element.
     *
     * @return move at the top of the stack
     */
    public final Move peek() {
        if (isEmpty()) {
            return null;
        }
        return get(size() - 1);
    }
}
