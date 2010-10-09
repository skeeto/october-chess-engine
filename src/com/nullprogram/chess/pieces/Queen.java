package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.MoveList;

/**
 * The Chess queen.
 *
 * This class describes the movement and capture behavior of the Chess
 * queen.
 */
public class Queen extends Piece {

    /**
     * Create a new queen on the given side.
     *
     * @param side piece owner
     */
    public Queen(final Side side) {
        super(side);
    }

    /** {@inheritDoc} */
    public final MoveList genMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        // Take advantage of the Bishop and Rook implementations
        list = Rook.genMoves(this, list);
        list = Bishop.genMoves(this, list);
        return list;
    }
}
