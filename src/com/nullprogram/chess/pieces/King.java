package com.nullprogram.chess.pieces;

import java.util.ArrayList;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class King extends Piece {
    public King(Side side) {
        super(side);
    }

    public ArrayList<Position> getMoves() {
        ArrayList<Position> list = new ArrayList<Position>();
        return list;
    }
}
