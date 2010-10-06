package com.nullprogram.chess.pieces;

import java.util.ArrayList;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.PositionList;

public class Pawn extends Piece {
    public Pawn(Side side) {
        super(side);
    }

    public PositionList getMoves() {
        PositionList list = new PositionList(getBoard());
        Position pos = getPosition();
        int dir = direction();
        list.addMove(new Position(pos.x, pos.y + 1 * dir));
        if (!moved()) {
            list.addMove(new Position(pos.x, pos.y + 2 * dir));
        }
        list.addCapture(new Position(pos.x - 1, pos.y + 1 * dir), getSide());
        list.addCapture(new Position(pos.x + 1, pos.y + 1 * dir), getSide());
        return list;
    }

    private Boolean moved() {
        Side side = getSide();
        Position pos = getPosition();
        return !((side == Side.WHITE && pos.y == 1) ||
                 (side == Side.BLACK && pos.y == 6));
    }

    private int direction() {
        if (getSide() == Side.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }
}
