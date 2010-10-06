package com.nullprogram.chess.boards;

import com.nullprogram.chess.Board;

/**
 * An empty Chess board.
 */
public class EmptyBoard extends Board {

    static final int DEFAULT_WIDTH = 8;
    static final int DEFAULT_HEIGHT = 8;

    public EmptyBoard() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public EmptyBoard(int width, int height) {
        setWidth(width);
        setHeight(height);
        clear();
    }
}
