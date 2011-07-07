package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.MoveList;

/**
 * The Chess chancellor.
 *
 * This class describes the movement and capture behavior of the Chess
 * chancellor (rook + knight).
 */
public class Chancellor extends Piece {
    /** Serial version. */
    private static final long serialVersionUID = 1769000361525074266L;

    /**
     * Create a new chancellor on the given side.
     *
     * @param side piece owner
     */
    public Chancellor(final Side side) {
        super(side, "Chancellor");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        /* Take advantage of the Rook and Knight implementations. */
        list = Rook.getMoves(this, list);
        list = Knight.getMoves(this, list);
        return list;
    }
}
