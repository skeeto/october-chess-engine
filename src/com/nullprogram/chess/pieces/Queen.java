package com.nullprogram.chess.pieces;

import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

/**
 * The Chess queen.
 *
 * This class describes the movement and capture behavior of the Chess
 * queen.
 */
public class Queen extends Piece {

    /** Serialization identifier. */
    private static final long serialVersionUID = -376604397L;

    /**
     * Create a new queen on the given side.
     *
     * @param side piece owner
     */
    public Queen(final Side side) {
        super(side, "Queen");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        /* Take advantage of the Bishop and Rook implementations. */
        list = Rook.getMoves(this, list);
        list = Bishop.getMoves(this, list);
        return list;
    }
}
