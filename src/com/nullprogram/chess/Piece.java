package com.nullprogram.chess;

import java.awt.image.BufferedImage;
import com.nullprogram.chess.pieces.ImageServer;

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

    public BufferedImage getImage(int size) {
        String name = this.getClass().getSimpleName();
        return ImageServer.getTile(name + "-" + side, size);
    }
}
