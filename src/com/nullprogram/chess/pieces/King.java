package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;

/**
 * The Chess king.
 *
 * This class describes the movement and capture behavior of the Chess
 * king.
 *
 * TODO: check
 * TODO: castling
 */
public class King extends Piece {

    /**
     * Create a new king on the given side.
     *
     * @param side piece owner
     */
    public King(final Side side) {
        super(side);
    }

    /** {@inheritDoc} */
    public final MoveList getMoves() {
        MoveList list = new MoveList(getBoard());
        Position pos = getPosition();
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x != 0 || y != 0) {
                    list.addCapture(new Move(pos, new Position(pos, x, y)));
                }
            }
        }
        return list;
    }
}
