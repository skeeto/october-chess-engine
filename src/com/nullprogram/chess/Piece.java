package com.nullprogram.chess;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import com.nullprogram.chess.pieces.ImageServer;

public abstract class Piece {

    public enum Side {
        WHITE, BLACK;
    }

    private Side side;
    private Position pos;

    protected Piece() {
    }

    protected Piece(Side side) {
        this.side = side;
    }

    public abstract ArrayList<Position> getMoves();

    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public Position getPosition() {
        return pos;
    }

    public Side getSide() {
        return side;
    }

    public BufferedImage getImage(int size) {
        String name = this.getClass().getSimpleName();
        return ImageServer.getTile(name + "-" + side, size);
    }
}
