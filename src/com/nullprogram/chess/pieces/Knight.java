package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.PositionList;

/**
 * The Chess knight.
 *
 * This class describes the movement and capture behavior of the Chess
 * knight.
 */
public class Knight extends Piece {

    public Knight(Side side) {
        super(side);
    }

    public PositionList getMoves() {
        PositionList list = new PositionList(getBoard());
        Position pos = getPosition();
        list.addMove(new Position(pos,  1,  2), getSide());
        list.addMove(new Position(pos,  2,  1), getSide());
        list.addMove(new Position(pos, -2,  1), getSide());
        list.addMove(new Position(pos, -2, -1), getSide());
        list.addMove(new Position(pos,  2, -1), getSide());
        list.addMove(new Position(pos,  1, -2), getSide());
        list.addMove(new Position(pos, -1, -2), getSide());
        list.addMove(new Position(pos, -1,  2), getSide());
        return list;
    }
}
