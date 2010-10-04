package com.nullprogram.chess.pieces;

import java.util.ArrayList;

import com.nullprogram.chess.Position;
import com.nullprogram.chess.Piece;

public class Pawn extends Piece {
    public Pawn(Side side) {
        super(side);
    }

    public ArrayList<Position> getMoves() {
        ArrayList<Position> list = new ArrayList<Position>();
        Position pos = getPosition();
        Side side = getSide();
        if (side == Side.WHITE) {
            list.add(new Position(pos.x, pos.y + 1));
            if (pos.x == 1) {
                list.add(new Position(pos.x, pos.y + 2));
            }
        } else {
            list.add(new Position(pos.x, pos.y - 1));
            if (pos.x == 6) {
                list.add(new Position(pos.x, pos.y - 2));
            }
        }
        return list;
    }
}
