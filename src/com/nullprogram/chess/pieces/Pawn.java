package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;

/**
 * The Chess pawn.
 *
 * This class describes the movement and capture behavior of the pawn
 * chess piece.
 *
 * TODO: en passant
 */
public class Pawn extends Piece {

    /**
     * Create a new pawn on the given side.
     *
     * @param side piece owner
     */
    public Pawn(final Side side) {
        super(side);
    }

    /** {@inheritDoc} */
    public final MoveList getMoves() {
        MoveList list = new MoveList(getBoard());
        Position pos = getPosition();
        int dir = direction();
        if (list.addMove(new Move(pos, new Position(pos, 0, 1 * dir)))) {
            if (!moved()) {
                list.addMove(new Move(pos, new Position(pos, 0, 2 * dir)));
            }
        }
        list.addCaptureOnly(new Move(pos, new Position(pos, -1, 1 * dir)));
        list.addCaptureOnly(new Move(pos, new Position(pos,  1, 1 * dir)));
        return list;
    }

    /**
     * Determine direction of this pawn's movement.
     *
     * @return direction for this pawn
     */
    private int direction() {
        if (getSide() == Side.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }
}
