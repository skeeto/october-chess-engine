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

    /**
     * Create a new chancellor on the given side.
     *
     * @param side piece owner
     */
    public Chancellor(final Side side) {
        super(side);
    }

    /** {@inheritDoc} */
    public final MoveList genMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        // Take advantage of the Rook and Knight implementations
        list = Rook.genMoves(this, list);
        list = Knight.genMoves(this, list);
        return list;
    }
}
