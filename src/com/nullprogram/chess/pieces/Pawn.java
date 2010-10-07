package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.PositionList;

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
     * Row for white pawns.
     */
    static final int WHITE_ROW = 1;

    /**
     * Row for black pawns.
     */
    static final int BLACK_ROW = 6;

    /**
     * Create a new pawn on the given side.
     *
     * @param side piece owner
     */
    public Pawn(final Side side) {
        super(side);
    }

    /** {@inheritDoc} */
    public final PositionList getMoves() {
        PositionList list = new PositionList(getBoard());
        Position pos = getPosition();
        int dir = direction();
        if (list.addMove(new Position(pos.getX(), pos.getY() + 1 * dir))) {
            if (!moved()) {
                list.addMove(new Position(pos.getX(), pos.getY() + 2 * dir));
            }
        }
        list.addCapture(new Position(pos.getX() - 1, pos.getY() + 1 * dir),
                        getSide());
        list.addCapture(new Position(pos.getX() + 1, pos.getY() + 1 * dir),
                        getSide());
        return list;
    }

    /**
     * Determine if the pawn has moved or not yet.
     *
     * @return true if piece has moved
     */
    private Boolean moved() {
        Side side = getSide();
        Position pos = getPosition();
        return !((side == Side.WHITE && pos.getY() == WHITE_ROW)
                 || (side == Side.BLACK && pos.getY() == BLACK_ROW));
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
