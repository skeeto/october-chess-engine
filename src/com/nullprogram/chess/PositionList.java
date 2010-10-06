package com.nullprogram.chess;

import java.util.ArrayList;

/**
 * Safe list of board positions.
 *
 * Before a position is added it is checked for validity.
 */
public class PositionList extends ArrayList<Position> {

    private static final long serialVersionUID = 1L;
    private Board board;

    public PositionList(Board board) {
        this.board = board;
    }

    /**
     * Add position to list if piece can legally move there (no capture).
     *
     * @param pos position to be added
     */
    public boolean addMove(Position pos) {
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
     */
    public boolean addMove(Position pos, Piece.Side side) {
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
     */
    public boolean addCapture(Position pos, Piece.Side side) {
        if (board.isFree(pos, side) && !board.isFree(pos)) {
            super.add(pos);
            return true;
        }
        return false;
    }
}
