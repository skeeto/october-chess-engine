package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.PositionList;

/**
 * The Chess king.
 *
 * This class describes the movement and capture behavior of the Chess
 * king.
 *
 * TODO: check
 */
public class King extends Piece {
    public King(Side side) {
        super(side);
    }

    public PositionList getMoves() {
        PositionList list = new PositionList(getBoard());
        Position pos = getPosition();
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x != 0 || y != 0) {
                    list.addMove(new Position(pos, x, y), getSide());
                }
            }
        }
        return list;
    }
}
