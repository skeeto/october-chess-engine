package com.nullprogram.chess.pieces;

import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

/**
 * The Chess archbishop.
 *
 * This class describes the movement and capture behavior of the Chess
 * archbishop (bishop + knight).
 */
public class Archbishop extends Piece {

    /** Serialization identifier. */
    private static final long serialVersionUID = -172677440L;

    /**
     * Create a new archbishop on the given side.
     *
     * @param side piece owner
     */
    public Archbishop(final Side side) {
        super(side, "Archbishop");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        /* Take advantage of the Bishop and Knight implementations */
        list = Bishop.getMoves(this, list);
        list = Knight.getMoves(this, list);
        return list;
    }
}
