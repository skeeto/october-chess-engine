package com.nullprogram.chess;

import java.util.ArrayList;

/**
 * Safe list of board positions.
 *
 * Before a position is added it is checked for validity.
 */
public class PositionList extends ArrayList<Position> {

    /**
     * Versioning for object serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The board used to verify positions before adding them.
     */
    private Board board;

    /**
     * Create a new position list relative to a board.
     *
     * @param verifyBoard the board to be used
     */
    public PositionList(final Board verifyBoard) {
        board = verifyBoard;
    }

    /**
     * Add position to list if piece can legally move there (no capture).
     *
     * @param pos position to be added
     * @return    true if position was added
     */
    public final boolean addMove(final Position pos) {
        if (board.isFree(pos)) {
            super.add(pos);
            return true;
        }
        return false;
    }

    /**
     * Add position to list if piece can move <i>or</i> capture at positon.
     *
     * @param pos  position to be added
     * @param side side of the piece making the move
     * @return     true if position was added
     */
    public final boolean addMove(final Position pos, final Piece.Side side) {
        if (board.isFree(pos, side)) {
            super.add(pos);
            return true;
        }
        return false;
    }

    /**
     * Add position to list only if the piece will perform a capture.
     *
     * @param pos  position to be added
     * @param side side of the piece making the move
     * @return     true if position was added
     */
    public final boolean addCapture(final Position pos,
                                    final Piece.Side side) {
        if (board.isFree(pos, side) && !board.isFree(pos)) {
            super.add(pos);
            return true;
        }
        return false;
    }
}
