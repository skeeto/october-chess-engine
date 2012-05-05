package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;

/**
 * Creates pieces based on their name strings.
 */
public final class PieceFactory {

    /** Hidden constructor. */
    private PieceFactory() {
    }

    /**
     * Create a new piece by name.
     *
     * @param name name of the piece
     * @param side side for the new piece
     * @return the new piece
     */
    public static Piece create(final String name, final Piece.Side side) {
        if ("Queen".equals(name)) {
            return new Queen(side);
        } else {
            /* Maybe throw an exception here? */
            return null;
        }
    }
}
