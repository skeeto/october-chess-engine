package com.nullprogram.chess;

public abstract class Piece {

    public enum Side {
        WHITE, BLACK;
    }

    private Side side;

    protected Piece() {
    }

    protected Piece(Side side) {
        this.side = side;
    }
}
