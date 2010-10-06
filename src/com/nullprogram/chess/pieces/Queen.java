package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.PositionList;

public class Queen extends Piece {
    public Queen(Side side) {
        super(side);
    }

    public PositionList getMoves() {
        PositionList list = new PositionList(getBoard());
        return list;
    }
}
