package com.nullprogram.chess.pieces;

import java.util.ArrayList;

import com.nullprogram.chess.Position;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Board;

public class Pawn extends Piece {
    public Pawn(Side side) {
        super(side);
    }

    public ArrayList<Position> getMoves() {
        ArrayList<Position> list = new ArrayList<Position>();
        Board b = getBoard();
        Position pos = getPosition();
        int dir = direction();
        Position first = new Position(pos.x, pos.y + 1 * dir);
        if (b.isEmpty(first) && b.inRange(first)) {
            list.add(first);
        }
        Position second = new Position(pos.x, pos.y + 2 * dir);
        if (!moved() && b.isEmpty(first) && b.inRange(first)) {
            list.add(second);
        }
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
